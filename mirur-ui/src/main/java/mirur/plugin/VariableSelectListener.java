package mirur.plugin;

import mirur.core.PrimitiveArray;
import mirur.core.PrimitiveTest;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.model.IIndexedValue;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.ui.AbstractDebugView;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.DebugContextEvent;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.debug.ui.contexts.IDebugContextService;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class VariableSelectListener implements ISelectionListener, INullSelectionListener, IDebugEventSetListener, IDebugContextListener {
    private static final String VARIABLE_VIEW_ID = "org.eclipse.debug.ui.VariableView";

    public void install(IWorkbenchWindow window) {
        DebugPlugin.getDefault().addDebugEventListener(this);

        IDebugContextService service = DebugUITools.getDebugContextManager().getContextService(window);
        service.addDebugContextListener(this);

        window.getSelectionService().addPostSelectionListener(VARIABLE_VIEW_ID, this);
    }

    public void uninstall(IWorkbenchWindow window) {
        DebugPlugin.getDefault().removeDebugEventListener(this);

        IDebugContextService service = DebugUITools.getDebugContextManager().getContextService(window);
        service.removeDebugContextListener(this);

        window.getSelectionService().removePostSelectionListener(VARIABLE_VIEW_ID, this);

        Activator.getVariableCache().clear();
    }

    private void update() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IDebugContextService service = DebugUITools.getDebugContextManager().getContextService(window);
        ISelection contextSelection = service.getActiveContext();
        IJavaStackFrame frame = extract(contextSelection, IJavaStackFrame.class);

        IJavaVariable variable = null;
        IViewPart view = window.getActivePage().findView(VARIABLE_VIEW_ID);
        if (view instanceof AbstractDebugView) {
            ISelection varSelection = ((AbstractDebugView) view).getViewer().getSelection();
            variable = extract(varSelection, IJavaVariable.class);
        }

        if (frame == null || variable == null) {
            Activator.getSelectionModel().select(null);
        } else {
            try {
                IValue value = variable.getValue();
                String varName = variable.getName();

                if (Activator.getVariableCache().contains(variable, frame)) {
                    PrimitiveArray array = Activator.getVariableCache().getArray(variable, frame);
                    Activator.getSelectionModel().select(array);
                } else if (value instanceof IJavaValue) {
                    new ReceiveArrayJob(varName, variable, frame).schedule();
                } else if (isPrimitiveArray(value)) {
                    new CopyJDIArrayJob(variable, (IIndexedValue) value, frame).schedule();
                } else {
                    Activator.getSelectionModel().select(null);
                }
            } catch (DebugException ex) {
                Activator.getSelectionModel().select(null);
                throw new VariableTransferException(ex);
            }
        }
    }

    private boolean isPrimitiveArray(IValue value) throws DebugException {
        if (value instanceof IIndexedValue) {
            String refTypeName = ((IIndexedValue) value).getReferenceTypeName();
            System.out.println(refTypeName);
        } else if (value instanceof IJavaArray) {
            IJavaArrayType arrayType = (IJavaArrayType) ((IJavaArray) value).getJavaType();
            IJavaType componentType = arrayType.getComponentType();
            if (PrimitiveTest.isPrimitiveName(componentType.getName())) {
                return true;
            } else if (componentType instanceof IJavaArrayType) {
                IJavaType component2Type = ((IJavaArrayType) componentType).getComponentType();
                return PrimitiveTest.isPrimitiveName(component2Type.getName());
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private <T> T extract(ISelection selection, Class<T> type) {
        if (selection instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) selection).getFirstElement();
            if (type.isInstance(firstElement)) {
                return (T) firstElement;
            }
        }

        return null;
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        update();
    }

    @Override
    public void debugContextChanged(DebugContextEvent event) {
        if (event.getFlags() == DebugContextEvent.ACTIVATED) {
            update();
        }
    }

    @Override
    public void handleDebugEvents(DebugEvent[] events) {
        if (events.length == 1) {
            DebugEvent event = events[0];
            int kind = event.getKind();
            if ((kind == DebugEvent.RESUME && event.getDetail() == DebugEvent.EVALUATION_IMPLICIT) || kind == DebugEvent.SUSPEND) {
                // probably generating a .toString on the variable
            } else {
                Activator.getVariableCache().clear();
                if (kind == DebugEvent.TERMINATE && event.getSource() instanceof IJavaDebugTarget) {
                    Activator.getAgentDeployer().clear((IJavaDebugTarget) event.getSource());
                }
            }
        } else {
            for (DebugEvent e : events) {
                handleDebugEvents(new DebugEvent[] { e });
            }
        }
    }

    public void forceUpdateNotify() {
        update();
    }
}

package mirur.plugin;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.ui.AbstractDebugView;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.DebugContextEvent;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.debug.ui.contexts.IDebugContextService;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;
import org.eclipse.jdt.internal.debug.core.model.JDIVariable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class VariableSelectListener implements ISelectionListener, INullSelectionListener, IDebugEventSetListener, IDebugContextListener {
    private static final String VARIABLE_VIEW_ID = "org.eclipse.debug.ui.VariableView";

    private final SelectionCache cache = new SelectionCache();

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

        cache.clear();
    }

    private void update() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IDebugContextService service = DebugUITools.getDebugContextManager().getContextService(window);
        ISelection contextSelection = service.getActiveContext();
        JDIStackFrame frame = extract(contextSelection, JDIStackFrame.class);

        JDIVariable variable = null;
        IViewPart view = window.getActivePage().findView(VARIABLE_VIEW_ID);
        if (view instanceof AbstractDebugView) {
            ISelection varSelection = ((AbstractDebugView) view).getViewer().getSelection();
            variable = extract(varSelection, JDIVariable.class);
        }

        if (frame == null || variable == null) {
            Model.MODEL.select(null);
        } else {
            try {
                new CopyJDIArrayJob(cache, variable, frame).schedule();
            } catch (DebugException ex) {
                throw new RuntimeException(ex);
            }
        }
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
        update();
    }

    @Override
    public void handleDebugEvents(DebugEvent[] events) {
        if (events.length == 1) {
            DebugEvent event = events[0];
            int kind = event.getKind();
            if ((kind == DebugEvent.RESUME && event.getDetail() == DebugEvent.EVALUATION_IMPLICIT) || kind == DebugEvent.SUSPEND) {
                // probably generating a .toString on the variable
            } else {
                cache.clear();
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

package testplugin.views;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.ui.AbstractDebugView;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.DebugContextEvent;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.debug.ui.contexts.IDebugContextService;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IViewSite;

public class VariablesView extends AbstractDebugView implements IDebugContextListener {
    private TreeViewer treeViewer;
    private IContentProvider treeContentProvider;
    private DebugViewListener listener = new SysoutDebugViewListener();

    @Override
    public Viewer createViewer(Composite parent) {
        treeContentProvider = new ILazyTreeContentProvider() {
            TreeViewer viewer;

            VarTreeNode[] vars;

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                this.viewer = (TreeViewer) viewer;
                vars = (VarTreeNode[]) newInput;

                if (vars == null) {
                    vars = new VarTreeNode[0];
                }
            }

            @Override
            public void dispose() {
                viewer = null;
                vars = null;
            }

            @Override
            public void updateElement(Object parent, int index) {
                viewer.replace(parent, index, vars[index]);
            }

            @Override
            public void updateChildCount(Object element, int currentChildCount) {
                if (element instanceof VarTreeNode[]) {
                    viewer.setChildCount(element, vars.length);
                } else {
                    viewer.setChildCount(element, 0);
                }
            }

            @Override
            public Object getParent(Object element) {
                return null;
            }
        };

        int style = SWT.VIRTUAL | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION;
        treeViewer = new TreeViewer(parent, style);
        Tree tree = treeViewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);

        TreeColumn col = new TreeColumn(tree, SWT.LEFT);
        col.setText("Name");
        col.setWidth(200);
        col = new TreeColumn(tree, SWT.LEFT);
        col.setText("Value");
        col.setWidth(300);

        treeViewer.setContentProvider(treeContentProvider);
        ITableLabelProvider lblProvider = new ITableLabelProvider() {
            @Override
            public void removeListener(ILabelProviderListener listener) {
            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            @Override
            public void dispose() {
            }

            @Override
            public void addListener(ILabelProviderListener listener) {
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                switch (columnIndex) {
                case 0:
                    return ((VarTreeNode) element).name;

                case 1:
                    return ((VarTreeNode) element).value;

                default:
                    throw new AssertionError("Invalid column index " + columnIndex);
                }
            }

            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }
        };

        treeViewer.setLabelProvider(lblProvider);

        treeViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelectionProvider() == treeViewer) {
                    treeSelectionChanged(event.getSelection());
                }
            }
        });

        DebugUITools.addPartDebugContextListener(getSite(), this);

        return treeViewer;
    }

    protected void treeSelectionChanged(ISelection selection) {
        VarTreeNode node = (VarTreeNode) ((IStructuredSelection) selection).getFirstElement();

        listener.clear();

        try {
            if (node != null) {
                IValue value = node.var.getValue();
                if (isNativeArray(value)) {
                    Object primitiveArray = toPrimitiveArray(value);
                    if (primitiveArray instanceof int[]) {
                        listener.inspect((int[]) primitiveArray);
                    } else if (primitiveArray instanceof float[]) {
                        listener.inspect((float[]) primitiveArray);
                    } else if (primitiveArray instanceof long[]) {
                        listener.inspect((long[]) primitiveArray);
                    } else if (primitiveArray instanceof short[]) {
                        listener.inspect((short[]) primitiveArray);
                    } else if (primitiveArray instanceof double[]) {
                        listener.inspect((double[]) primitiveArray);
                    } else if (primitiveArray instanceof char[]) {
                        listener.inspect((char[]) primitiveArray);
                    } else if (primitiveArray instanceof byte[]) {
                        listener.inspect((byte[]) primitiveArray);
                    }
                }
            }
        } catch (DebugException ex) {
            // XXX how to handle?
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void dispose() {
        DebugUITools.removePartDebugContextListener(getSite(), this);
        super.dispose();
    }

    protected ISelection getDebugContext() {
        IViewSite site = (IViewSite) getSite();
        IDebugContextService contextService = DebugUITools.getDebugContextManager().getContextService(site.getWorkbenchWindow());
        return contextService.getActiveContext(site.getId(), site.getSecondaryId());
    }

    @Override
    protected String getHelpContextId() {
        // TODO
        return "TODO";
    }

    @Override
    protected void createActions() {
    }

    @Override
    protected void fillContextMenu(IMenuManager menu) {
    }

    @Override
    public void debugContextChanged(DebugContextEvent event) {
        if ((event.getFlags() & DebugContextEvent.ACTIVATED) > 0) {
            contextActivated(event.getContext());
        }
    }

    private void contextActivated(ISelection selection) {
        Object treeData = null;
        if (selection instanceof IStructuredSelection) {
            Object source = ((IStructuredSelection) selection).getFirstElement();
            if (source instanceof JDIStackFrame) {
                JDIStackFrame frame = (JDIStackFrame) source;
                try {
                    treeData = convert(frame);
                } catch (DebugException ex) {
                    // TODO how to handle properly
                    throw new RuntimeException(ex);
                }
            }
        }

        treeViewer.setInput(treeData);
    }

    private VarTreeNode[] convert(JDIStackFrame frame) throws DebugException {
        IVariable[] iVars = frame.getVariables();
        VarTreeNode[] vars = new VarTreeNode[iVars.length];

        for (int i = 0; i < iVars.length; i++) {
            IVariable iVar = iVars[i];
            String name = iVar.getName();
            String type = iVar.getReferenceTypeName();
            IValue val = iVar.getValue();
            String valueStr = isPrimitive(val) ? val.getValueString() : type;

            vars[i] = new VarTreeNode(iVar, name, valueStr);
        }

        return vars;
    }

    private boolean isPrimitive(IValue value) throws DebugException {
        return value instanceof IJavaPrimitiveValue;
    }

    private Object toPrimitiveArray(IValue value) throws DebugException {
        IJavaArray val = (IJavaArray) value;
        int len = val.getLength();

        IJavaType componentType = ((IJavaArrayType) val.getJavaType()).getComponentType();
        switch (componentType.getName()) {
        case "int": {
            int[] v = new int[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getIntValue();
            }
            return v;
        }
        case "float": {
            float[] v = new float[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getFloatValue();
            }
            return v;
        }
        case "double": {
            double[] v = new double[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getDoubleValue();
            }
            return v;
        }
        case "short": {
            short[] v = new short[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getShortValue();
            }
            return v;
        }
        case "long": {
            long[] v = new long[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getLongValue();
            }
            return v;
        }
        case "byte": {
            byte[] v = new byte[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getByteValue();
            }
            return v;
        }
        case "char": {
            char[] v = new char[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getCharValue();
            }
            return v;
        }

        default:
            throw new AssertionError("Unexpected type");
        }
    }

    private boolean isNativeArray(IValue value) throws DebugException {
        if (value instanceof IJavaArray) {
            IJavaType componentType = ((IJavaArrayType) (((IJavaArray) value).getJavaType())).getComponentType();
            switch (componentType.getName()) {
            case "int":
            case "float":
            case "double":
            case "short":
            case "long":
            case "byte":
            case "char":
                return true;

            default:
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void becomesHidden() {
        contextActivated(null);
        super.becomesHidden();
    }

    @Override
    protected void becomesVisible() {
        super.becomesVisible();
        ISelection selection = getDebugContext();
        contextActivated(selection);
    }

    @Override
    protected void configureToolBar(IToolBarManager tbm) {
    }

    private static class VarTreeNode {
        public final IVariable var;
        public final String name;
        public final String value;

        public VarTreeNode(IVariable var, String name, String value) {
            this.var = var;
            this.name = name;
            this.value = value;
        }
    }
}

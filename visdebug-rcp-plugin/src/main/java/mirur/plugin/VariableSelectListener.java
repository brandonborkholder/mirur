package mirur.plugin;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.DebugContextEvent;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.debug.ui.contexts.IDebugContextService;
import org.eclipse.jdt.internal.debug.core.model.JDILocalVariable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

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

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof TreeSelection) {
            Object o = ((TreeSelection) selection).getFirstElement();

            if (o instanceof JDILocalVariable) {
                try {
                    new CopyJDIArrayJob(cache, (JDILocalVariable) o).schedule();
                } catch (DebugException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                Model.MODEL.select(null);
            }
        }
    }

    @Override
    public void debugContextChanged(DebugContextEvent event) {
        // TODO Not implemented yet...
        throw new AssertionError("Not implemented yet...");
    }

    @Override
    public void handleDebugEvents(DebugEvent[] events) {
        // TODO Not implemented yet...
        throw new AssertionError("Not implemented yet...");
    }
}

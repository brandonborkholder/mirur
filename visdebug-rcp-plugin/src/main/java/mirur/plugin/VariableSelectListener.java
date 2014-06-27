package mirur.plugin;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.internal.debug.core.model.JDILocalVariable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

@SuppressWarnings("restriction")
public class VariableSelectListener implements ISelectionListener, INullSelectionListener {
    private final SelectionCache cache = new SelectionCache();

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
}

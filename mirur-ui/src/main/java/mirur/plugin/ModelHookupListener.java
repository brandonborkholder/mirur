package mirur.plugin;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

public class ModelHookupListener implements IPartListener2 {
    private final String partID;
    private final ViewPart part;
    private final ArraySelectListener listener;

    public static <T extends ViewPart & ArraySelectListener> void install(String partId, T part) {
        part.getSite().getPage().addPartListener(new ModelHookupListener(partId, part, part));
    }

    public ModelHookupListener(String partID, ViewPart part, ArraySelectListener listener) {
        this.partID = partID;
        this.part = part;
        this.listener = listener;
    }

    @Override
    public void partActivated(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partBroughtToTop(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partClosed(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            Activator.getSelectionModel().removeArrayListener(part, listener);

            part.getSite().getPage().removePartListener(this);
        }
    }

    @Override
    public void partDeactivated(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partOpened(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            Activator.getSelectionModel().addArrayListener(part, listener);
        }
    }

    @Override
    public void partHidden(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            Activator.getSelectionModel().removeArrayListener(part, listener);
        }
    }

    @Override
    public void partVisible(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            Activator.getSelectionModel().addArrayListener(part, listener);
        }
    }

    @Override
    public void partInputChanged(IWorkbenchPartReference partRef) {
    }
}

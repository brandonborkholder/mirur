package mirur.plugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class SelectListenerToggle extends Action implements IPartListener2 {
    private final String partID;
    private final ViewPart part;
    private final ArraySelectListener listener;

    public SelectListenerToggle(String partID, ViewPart part, ArraySelectListener listener) {
        super("Sync Viewer", IAction.AS_CHECK_BOX);
        setId(SelectListenerToggle.class.getName());
        setText("Sync Viewer");
        setToolTipText("Sync with Variables Selection");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));

        this.partID = partID;
        this.part = part;
        this.listener = listener;

        setChecked(true);
    }

    @Override
    public void partActivated(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partDeactivated(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partBroughtToTop(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partClosed(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            remove();
        }
    }

    @Override
    public void partOpened(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            add(false);
        }
    }

    @Override
    public void partHidden(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            remove();
        }
    }

    @Override
    public void partVisible(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            add(false);
        }
    }

    @Override
    public void partInputChanged(IWorkbenchPartReference partRef) {
    }

    private void add(boolean force) {
        if (isChecked() || force) {
            Activator.getSelectionModel().addArrayListener(part, listener);
        }
    }

    private void remove() {
        Activator.getSelectionModel().removeArrayListener(part, listener);
    }

    @Override
    public void run() {
        if (isChecked()) {
            add(true);
        } else {
            remove();
        }
    }
}

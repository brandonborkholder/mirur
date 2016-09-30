package mirur.plugin.painterview;

import java.util.UUID;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import mirur.plugin.Icons;

public abstract class CreateStaticViewAction extends Action {
    public CreateStaticViewAction() {
        setId(CreateStaticViewAction.class.getName());
        setText("New Painter View");
        setToolTipText("Pin To New View");
        setImageDescriptor(Icons.getPin(true));
        setDisabledImageDescriptor(Icons.getPin(false));
    }

    @Override
    public void run() {
        try {
            IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView(StaticArrayView.ID, UUID.randomUUID().toString(), IWorkbenchPage.VIEW_ACTIVATE);
            initializeView((StaticArrayView) view);
        } catch (PartInitException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected abstract void initializeView(StaticArrayView view);
}

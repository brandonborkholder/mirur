package mirur.plugin.statsview;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ShowArrayDataToggle extends Action {
    public ShowArrayDataToggle() {
        super("Array Values", IAction.AS_CHECK_BOX);
        setId(ShowArrayDataToggle.class.getName());
        setToolTipText("Show Values as Table");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT));
    }
}

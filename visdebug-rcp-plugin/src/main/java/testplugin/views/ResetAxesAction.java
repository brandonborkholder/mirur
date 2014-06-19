package testplugin.views;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public abstract class ResetAxesAction extends Action {
    public ResetAxesAction() {
        setId(ResetAxesAction.class.getName());
        setText("Reset Axes");
        setToolTipText("Reset Plot Axes");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_CLEAR));
    }
}

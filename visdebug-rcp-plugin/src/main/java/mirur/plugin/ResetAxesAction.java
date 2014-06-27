package mirur.plugin;

import org.eclipse.jface.action.Action;

public abstract class ResetAxesAction extends Action {
    public ResetAxesAction() {
        setId(ResetAxesAction.class.getName());
        setText("Reset Axes");
        setToolTipText("Reset Plot Axes");
        setImageDescriptor(Activator.getImageDescriptor(Icons.RESET_PATH));
    }
}

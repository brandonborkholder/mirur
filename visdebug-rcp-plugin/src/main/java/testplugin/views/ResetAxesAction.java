package testplugin.views;

import org.eclipse.jface.action.Action;

import test_plugin.Activator;

public abstract class ResetAxesAction extends Action {
    public ResetAxesAction() {
        setId(ResetAxesAction.class.getName());
        setText("Reset Axes");
        setToolTipText("Reset Plot Axes");
        setImageDescriptor(Activator.getImageDescriptor(Icons.RESET_PATH));
    }
}

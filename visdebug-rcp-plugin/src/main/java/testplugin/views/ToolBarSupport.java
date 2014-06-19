package testplugin.views;

import org.eclipse.jface.action.ToolBarManager;

public class ToolBarSupport {
    private ResetAxesAction resetAxes;

    public ToolBarSupport(ToolBarManager manager) {
        resetAxes = new ResetAxesAction();
        manager.add(resetAxes);
    }

    public ResetAxesAction getResetAxesAction() {
        return resetAxes;
    }
}

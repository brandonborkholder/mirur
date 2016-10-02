package mirur.plugins.xyscatter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class ShowAsDensityAction extends Action {
    private final XyPointsPainter pointsPainter;

    public ShowAsDensityAction(XyPointsPainter pointsPainter) {
        super("Show As Density", IAction.AS_CHECK_BOX);
        this.pointsPainter = pointsPainter;
        setId(ShowAsDensityAction.class.getName());
    }

    @Override
    public void run() {
        pointsPainter.setShowAsDensity(isChecked());
    }
}

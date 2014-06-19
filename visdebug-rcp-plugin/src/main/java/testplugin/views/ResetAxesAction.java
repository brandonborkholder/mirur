package testplugin.views;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ResetAxesAction extends Action {
    private List<Runnable> listeners = new CopyOnWriteArrayList<>();

    public ResetAxesAction() {
        setId(ResetAxesAction.class.getName());
        setText("Reset Axes");
        setToolTipText("Reset Plot Axes");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_CLEAR));
    }

    public void addListener(Runnable l) {
        listeners.add(l);
    }

    public void removeListener(Runnable l) {
        listeners.remove(l);
    }

    @Override
    public void run() {
        for (Runnable l : listeners) {
            l.run();
        }
    }
}

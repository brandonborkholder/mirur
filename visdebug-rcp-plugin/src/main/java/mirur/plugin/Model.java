package mirur.plugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;

public class Model {
    public static final Model MODEL = new Model();

    private VariableSelectListener varListener;
    private boolean isVarListenerAttached;

    private List<ArraySelectListener> arrayListeners;

    private PrimitiveArray lastSelected;

    private Model() {
        varListener = new VariableSelectListener();
        isVarListenerAttached = false;
        arrayListeners = new ArrayList<>();
    }

    public synchronized void addArrayListener(IViewPart part, ArraySelectListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }

        arrayListeners.add(listener);

        if (!isVarListenerAttached && !arrayListeners.isEmpty()) {
            varListener.install(part.getSite().getWorkbenchWindow());
            isVarListenerAttached = true;
        }

        notifySelectedAsync(listener, lastSelected);
    }

    public synchronized void removeArrayListener(IViewPart part, ArraySelectListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }

        arrayListeners.remove(listener);

        if (isVarListenerAttached && arrayListeners.isEmpty()) {
            varListener.uninstall(part.getSite().getWorkbenchWindow());
            isVarListenerAttached = false;
        }
    }

    private void notifySelectedAsync(final ArraySelectListener listener, final PrimitiveArray array) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                listener.arraySelected(array);
            }
        });
    }

    public void select(PrimitiveArray selected) {
        lastSelected = selected;
        for (ArraySelectListener l : arrayListeners) {
            notifySelectedAsync(l, selected);
        }
    }
}

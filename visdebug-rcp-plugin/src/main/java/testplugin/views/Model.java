package testplugin.views;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Model {
    public static final Model MODEL = new Model();

    private List<ArraySelectListener> arrayListeners = new CopyOnWriteArrayList<>();

    public void addArrayListener(ArraySelectListener listener) {
        arrayListeners.add(listener);
    }

    public void select(PrimitiveArray selected) {
        for (ArraySelectListener l : arrayListeners) {
            l.selected(selected);
        }
    }
}

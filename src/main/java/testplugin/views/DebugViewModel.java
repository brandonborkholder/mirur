package testplugin.views;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DebugViewModel {
    public static final DebugViewModel SINGLETON;

    private final List<DebugViewListener> listeners;

    static {
        SINGLETON = new DebugViewModel();
    }

    public DebugViewModel() {
        listeners = new CopyOnWriteArrayList<>();
    }

    public void addListener(DebugViewListener listener) {
        listeners.add(listener);
    }

    public void show(int[] data) {
    }
}
package testplugin.views;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DebugViewListeners implements DebugViewListener {
    public static DebugViewListeners SINGLETON = new DebugViewListeners();

    private List<DebugViewListener> listeners = new CopyOnWriteArrayList<>();

    public void add(DebugViewListener listener) {
        listeners.add(listener);
    }

    public void remove(DebugViewListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void inspect(int[] data) {
        for (DebugViewListener listener : listeners) {
            listener.inspect(data);
        }
    }

    @Override
    public void inspect(float[] data) {
        for (DebugViewListener listener : listeners) {
            listener.inspect(data);
        }
    }

    @Override
    public void inspect(short[] data) {
        for (DebugViewListener listener : listeners) {
            listener.inspect(data);
        }
    }

    @Override
    public void inspect(byte[] data) {
        for (DebugViewListener listener : listeners) {
            listener.inspect(data);
        }
    }

    @Override
    public void inspect(double[] data) {
        for (DebugViewListener listener : listeners) {
            listener.inspect(data);
        }
    }

    @Override
    public void inspect(long[] data) {
        for (DebugViewListener listener : listeners) {
            listener.inspect(data);
        }
    }

    @Override
    public void inspect(char[] data) {
        for (DebugViewListener listener : listeners) {
            listener.inspect(data);
        }
    }

    @Override
    public void clear() {
        for (DebugViewListener listener : listeners) {
            listener.clear();
        }
    }
}

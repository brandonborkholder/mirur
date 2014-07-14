package mirur.plugin;

import java.util.HashMap;
import java.util.Map;

import mirur.core.PrimitiveArray;

import org.eclipse.jdt.debug.core.IJavaStackFrame;

public class SelectionCache {
    private Map<Key, PrimitiveArray> cache;

    public SelectionCache() {
        cache = new HashMap<>();
    }

    public void clear() {
        cache.clear();
    }

    public boolean contains(String name, IJavaStackFrame frame) {
        return cache.containsKey(new Key(name, frame));
    }

    public PrimitiveArray getArray(String name, IJavaStackFrame frame) {
        return cache.get(new Key(name, frame));
    }

    public void put(String name, IJavaStackFrame frame, PrimitiveArray array) {
        cache.put(new Key(name, frame), array);
    }

    private static class Key {
        final String name;
        final IJavaStackFrame frame;

        Key(String name, IJavaStackFrame frame) {
            this.name = name;
            this.frame = frame;
        }

        @Override
        public int hashCode() {
            return name.hashCode() * 13 + frame.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Key) {
                Key k = (Key) obj;
                return k.name.equals(name) && k.frame.equals(frame);
            } else {
                return false;
            }
        }
    }
}

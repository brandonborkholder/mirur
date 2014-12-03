package mirur.plugin;

import java.util.HashMap;
import java.util.Map;

import mirur.core.PrimitiveArray;

import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaStackFrame;

/**
 * We want to cache a variable that we've already transfered. But a variable is only cacheable as long as it's not
 * modified. So we cache the variable and stack frame. When the thread resumes, then the cache needs to be cleared.
 */
public class SelectionCache {
    private Map<Key, PrimitiveArray> cache;

    public SelectionCache() {
        cache = new HashMap<>();
    }

    public void clear() {
        cache.clear();
    }

    public boolean contains(IVariable variable, IJavaStackFrame frame) {
        return cache.containsKey(new Key(variable, frame));
    }

    public PrimitiveArray getArray(IVariable variable, IJavaStackFrame frame) {
        return cache.get(new Key(variable, frame));
    }

    public void put(IVariable variable, IJavaStackFrame frame, PrimitiveArray array) {
        cache.put(new Key(variable, frame), array);
    }

    private static class Key {
        final IVariable var;
        final IJavaStackFrame frame;

        Key(IVariable var, IJavaStackFrame frame) {
            this.var = var;
            this.frame = frame;
        }

        @Override
        public int hashCode() {
            return var.hashCode() * 13 + frame.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Key) {
                Key k = (Key) obj;
                return k.var.equals(var) && k.frame.equals(frame);
            } else {
                return false;
            }
        }
    }
}

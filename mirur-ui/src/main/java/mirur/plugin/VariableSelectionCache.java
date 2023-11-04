/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;

import mirur.core.VariableObject;

/**
 * We want to cache a variable that we've already transfered. But a variable is only cacheable as long as it's not
 * modified. So we cache the variable and stack frame. When the thread resumes, then the cache needs to be cleared.
 */
public class VariableSelectionCache {
    private Map<Key, VariableObject> cache;

    public VariableSelectionCache() {
        cache = new HashMap<>();
    }

    public void clear() {
        cache.clear();
    }

    public boolean contains(IVariable variable, IStackFrame frame) {
        return cache.containsKey(new Key(variable, frame));
    }

    public VariableObject getArray(IVariable variable, IStackFrame frame) {
        return cache.get(new Key(variable, frame));
    }

    public void put(IVariable variable, IStackFrame frame, VariableObject obj) {
        cache.put(new Key(variable, frame), obj);
    }

    private static class Key {
        final IVariable var;
        final IStackFrame frame;

        Key(IVariable var, IStackFrame frame) {
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

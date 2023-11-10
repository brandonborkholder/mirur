/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © 2023 Brandon Borkholder (support@mirur.io)
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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import mirur.core.VariableObject;
import mirur.plugins.MirurView;
import mirur.plugins.MirurViews;

public class ViewSelectionModel implements VarObjectSelectListener {
    private Deque<MirurView> lastSelectedQ;

    private List<ViewSelectListener> viewListeners;

    public ViewSelectionModel() {
        lastSelectedQ = new ArrayDeque<>();
        lastSelectedQ.addAll(MirurViews.plugins());
        viewListeners = new ArrayList<>();
    }

    public synchronized void addArrayListener(ViewSelectListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }

        if (viewListeners.contains(listener)) {
            return;
        }

        viewListeners.add(listener);
        notifySelectedAsync(listener, getActiveSelected());
    }

    public synchronized void removeArrayListener(ViewSelectListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }

        if (!viewListeners.contains(listener)) {
            return;
        }

        viewListeners.remove(listener);
        listener.viewSelected(null);
    }

    private void notifySelectedAsync(final ViewSelectListener listener, final MirurView view) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                listener.viewSelected(view);
            }
        });
    }

    public synchronized void select(MirurView selected) {
        if (getActiveSelected().equals(selected)) {
            return;
        }

        lastSelectedQ.remove(selected);
        lastSelectedQ.push(selected);

        for (ViewSelectListener l : viewListeners) {
            notifySelectedAsync(l, selected);
        }
    }

    public synchronized MirurView getActiveSelected() {
        return lastSelectedQ.peek();
    }

    @Override
    public synchronized void variableSelected(VariableObject obj) {
        if (obj == null) {
            return;
        }

        // find the last used view for this kind of data
        for (MirurView view : lastSelectedQ) {
            if (view.supportsData(obj)) {
                select(view);
                break;
            }
        }
    }

    @Override
    public void clearVariableCacheData() {
    }
}

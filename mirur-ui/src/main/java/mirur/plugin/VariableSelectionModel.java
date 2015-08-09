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

import java.util.ArrayList;
import java.util.List;

import mirur.core.PrimitiveArray;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;

public class VariableSelectionModel {
    private VariableSelectListener varListener;
    private boolean isVarListenerAttached;

    private List<ArraySelectListener> arrayListeners;
    private PrimitiveArray lastSelected;

    public VariableSelectionModel() {
        varListener = new VariableSelectListener();
        isVarListenerAttached = false;
        arrayListeners = new ArrayList<>();
    }

    public synchronized void addArrayListener(IViewPart part, ArraySelectListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }

        if (arrayListeners.contains(listener)) {
            return;
        }

        arrayListeners.add(listener);
        notifySelectedAsync(listener, lastSelected);

        if (!isVarListenerAttached && !arrayListeners.isEmpty()) {
            varListener.install(part.getSite().getWorkbenchWindow());
            isVarListenerAttached = true;
            varListener.forceUpdateNotify();
        }
    }

    public synchronized void removeArrayListener(IViewPart part, ArraySelectListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }

        if (!arrayListeners.contains(listener)) {
            return;
        }

        arrayListeners.remove(listener);
        listener.arraySelected(null);

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
        if (lastSelected == selected) {
            return;
        } else {
            lastSelected = selected;
        }

        Activator.getStatistics().selected(selected);

        for (ArraySelectListener l : arrayListeners) {
            notifySelectedAsync(l, selected);
        }
    }

    public PrimitiveArray getActiveSelected() {
        return lastSelected;
    }
}

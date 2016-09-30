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

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;

import mirur.core.VariableObject;

public class VariableSelectionModel {
    private VariableSelectListener varListener;
    private boolean isVarListenerAttached;

    private List<VarObjectSelectListener> arrayListeners;
    private VariableObject lastSelected;

    public VariableSelectionModel() {
        varListener = new VariableSelectListener();
        isVarListenerAttached = false;
        arrayListeners = new ArrayList<>();
    }

    public synchronized void addArrayListener(IViewPart part, VarObjectSelectListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }

        if (arrayListeners.contains(listener)) {
            return;
        }

        arrayListeners.add(listener);
        notifySelectedAsync(listener, lastSelected);

        if (!isVarListenerAttached && !arrayListeners.isEmpty() && part != null) {
            varListener.install(part.getSite().getWorkbenchWindow());
            isVarListenerAttached = true;
            varListener.forceUpdateNotify();
        }
    }

    public synchronized void removeArrayListener(IViewPart part, VarObjectSelectListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }

        if (!arrayListeners.contains(listener)) {
            return;
        }

        arrayListeners.remove(listener);
        listener.variableSelected(null);

        if (isVarListenerAttached && arrayListeners.isEmpty() && part != null) {
            varListener.uninstall(part.getSite().getWorkbenchWindow());
            isVarListenerAttached = false;
        }
    }

    private void notifySelectedAsync(final VarObjectSelectListener listener, final VariableObject obj) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                listener.variableSelected(obj);
            }
        });
    }

    public synchronized void select(VariableObject selected) {
        if (lastSelected == selected) {
            return;
        }

        lastSelected = selected;

        Activator.getStatistics().selected(selected);

        for (VarObjectSelectListener l : arrayListeners) {
            notifySelectedAsync(l, selected);
        }
    }

    public VariableObject getActiveSelected() {
        return lastSelected;
    }

    public void fireClearVariableCacheData() {
        select(null);
        for (final VarObjectSelectListener l : arrayListeners) {
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    l.clearVariableCacheData();
                }
            });
        }
    }
}

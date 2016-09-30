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

import static mirur.plugin.Activator.getPreferences;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

public class SelectListenerToggle extends Action implements IPartListener2, IPropertyChangeListener {
    public static final String SELECTED_KEY = "toggle.listen.array.selection";

    private final String partID;
    private final ViewPart part;
    private final VarObjectSelectListener listener;

    public SelectListenerToggle(String partID, ViewPart part, VarObjectSelectListener listener) {
        super("Sync Viewer", IAction.AS_CHECK_BOX);
        setId(SelectListenerToggle.class.getName());
        setText("Sync Viewer");
        setToolTipText("Sync with Variables Selection");
        setImageDescriptor(Icons.getSync(true));
        setDisabledImageDescriptor(Icons.getSync(false));

        this.partID = partID;
        this.part = part;
        this.listener = listener;

        setChecked(getPreferences().doSyncWithVariablesView(partID));
        getPreferences().addChangeListener(new Runnable() {
            @Override
            public void run() {
                setChecked(getPreferences().doSyncWithVariablesView(SelectListenerToggle.this.partID));
            }
        });

        addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (CHECKED.equals(event.getProperty())) {
            getPreferences().setSyncWithVariablesView(partID, (Boolean) event.getNewValue());
        }
    }

    @Override
    public void partActivated(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partDeactivated(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partBroughtToTop(IWorkbenchPartReference partRef) {
    }

    @Override
    public void partClosed(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            remove();
        }
    }

    @Override
    public void partOpened(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            add(false);
        }
    }

    @Override
    public void partHidden(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            remove();
        }
    }

    @Override
    public void partVisible(IWorkbenchPartReference partRef) {
        if (partID.equals(partRef.getId())) {
            add(false);
        }
    }

    @Override
    public void partInputChanged(IWorkbenchPartReference partRef) {
    }

    private void add(boolean force) {
        if (isChecked() || force) {
            Activator.getVariableSelectionModel().addArrayListener(part, listener);
        }
    }

    private void remove() {
        Activator.getVariableSelectionModel().removeArrayListener(part, listener);
    }

    @Override
    public void run() {
        if (isChecked()) {
            add(true);
        } else {
            remove();
        }
    }
}

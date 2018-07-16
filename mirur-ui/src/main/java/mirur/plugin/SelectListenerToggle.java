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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

public class SelectListenerToggle extends Action implements IPartListener2 {
    private final ViewPart part;
    private final VarObjectSelectListener listener;

    public SelectListenerToggle(ViewPart part, VarObjectSelectListener listener) {
        super("Sync Viewer", IAction.AS_CHECK_BOX);
        setId(SelectListenerToggle.class.getName());
        setText("Sync Viewer");
        setToolTipText("Sync with Variables Selection");
        setImageDescriptor(Icons.getSync(true));
        setDisabledImageDescriptor(Icons.getSync(false));

        this.part = part;
        this.listener = listener;

        setChecked(true);
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
        if (part.equals(partRef.getPart(false))) {
            remove();
        }
    }

    @Override
    public void partOpened(IWorkbenchPartReference partRef) {
        if (part.equals(partRef.getPart(false))) {
            add();
        }
    }

    @Override
    public void partHidden(IWorkbenchPartReference partRef) {
        if (part.equals(partRef.getPart(false))) {
            remove();
        }
    }

    @Override
    public void partVisible(IWorkbenchPartReference partRef) {
        if (part.equals(partRef.getPart(false))) {
            add();
        }
    }

    @Override
    public void partInputChanged(IWorkbenchPartReference partRef) {
    }

    private void add() {
        if (isChecked()) {
            Activator.getVariableSelectionModel().addArrayListener(part, listener);
        }
    }

    private void remove() {
        Activator.getVariableSelectionModel().removeArrayListener(part, listener);
    }

    @Override
    public void run() {
        if (isChecked()) {
            add();
        } else {
            remove();
        }
    }
}

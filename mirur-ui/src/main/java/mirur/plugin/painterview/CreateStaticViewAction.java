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
package mirur.plugin.painterview;

import java.util.UUID;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import mirur.plugin.Icons;

public abstract class CreateStaticViewAction extends Action {
    public CreateStaticViewAction() {
        setId(CreateStaticViewAction.class.getName());
        setText("New Painter View");
        setToolTipText("Pin To New View");
        setImageDescriptor(Icons.getPin(true));
        setDisabledImageDescriptor(Icons.getPin(false));
    }

    @Override
    public void run() {
        try {
            String uuid = UUID.randomUUID().toString();
            IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView(StaticArrayView.ID, uuid, IWorkbenchPage.VIEW_CREATE);
            initializeView((StaticArrayView) view);
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView(StaticArrayView.ID, uuid, IWorkbenchPage.VIEW_ACTIVATE);
        } catch (PartInitException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected abstract void initializeView(StaticArrayView view);
}

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

import static com.metsci.glimpse.util.logging.LoggerUtils.logWarning;

import java.util.logging.Logger;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import mirur.plugin.Icons;

public abstract class DuplicateViewAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(DuplicateViewAction.class.getName());

    private final String viewId;

    public DuplicateViewAction(String viewId) {
        this.viewId = viewId;
        setId(DuplicateViewAction.class.getName());
        setText("New View");
        setToolTipText("Create New Mirur View");
        setImageDescriptor(Icons.getDuplicateView(true));
        setDisabledImageDescriptor(Icons.getDuplicateView(false));
    }

    @Override
    public void run() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window != null) {
            IWorkbenchPage page = window.getActivePage();
            if (page == null) {
                return;
            }
            try {
                String id = viewId + "-" + System.nanoTime();
                IViewPart view = page.showView(viewId, id, 1);
                initializeView(view);
            } catch (PartInitException ex) {
                logWarning(LOGGER, "Error cloning view", ex);
            }
        }
    }

    protected abstract void initializeView(IViewPart view);
}
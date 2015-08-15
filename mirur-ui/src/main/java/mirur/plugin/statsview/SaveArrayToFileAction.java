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
package mirur.plugin.statsview;

import java.io.File;

import mirur.core.PrimitiveArray;
import mirur.core.VariableObject;
import mirur.plugin.Activator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class SaveArrayToFileAction extends Action {
    public SaveArrayToFileAction() {
        super("Save Data", IAction.AS_PUSH_BUTTON);
        setId(SaveArrayToFileAction.class.getName());
        setToolTipText("Save Array Values to File");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
    }

    @Override
    public void run() {
        VariableObject obj = Activator.getVariableSelectionModel().getActiveSelected();
        PrimitiveArray array = obj instanceof PrimitiveArray ? (PrimitiveArray) obj : null;
        if (array == null) {
            return;
        }

        FileDialog saveDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
        saveDialog.setFileName(array.getName() + ".csv");
        String destination = saveDialog.open();
        if (destination != null) {
            File fileDest = new File(destination);
            new SaveArrayToFileJob(array, fileDest).schedule();
        }
    }
}

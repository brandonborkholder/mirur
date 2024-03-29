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
package mirur.plugin.statsview;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import mirur.core.Array1D;
import mirur.core.Array2D;
import mirur.core.PrimitiveArray;
import mirur.core.VariableObject;
import mirur.plugin.Activator;
import mirur.plugin.Icons;
import mirur.plugin.VarObjectSelectListener;

public class SaveArrayToFileAction extends Action implements VarObjectSelectListener {
    public SaveArrayToFileAction() {
        super("Save Data", IAction.AS_PUSH_BUTTON);
        setId(SaveArrayToFileAction.class.getName());
        setToolTipText("Export Array to File");
        setImageDescriptor(Icons.getSaveAs(true));
        setDisabledImageDescriptor(Icons.getSaveAs(false));
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

    @Override
    public void variableSelected(VariableObject obj) {
        setEnabled(obj != null && (obj instanceof Array1D || obj instanceof Array2D));
    }

    @Override
    public void clearVariableCacheData() {
    }
}

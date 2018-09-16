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
package mirur.plugins.heatmap2d;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import mirur.plugin.Icons;
import mirur.plugins.DataPainterImpl.ResetAction;

public abstract class ToggleTransposeAction extends Action implements ResetAction {
    public ToggleTransposeAction() {
        super("Transpose", IAction.AS_CHECK_BOX);
        setId(ToggleTransposeAction.class.getName());
        setToolTipText("Transpose");
        setImageDescriptor(Icons.getRotate(true));
        setDisabledImageDescriptor(Icons.getRotate(false));
    }

    @Override
    public void reset() {
        setChecked(false);
        run();
    }

    @Override
    public void validate() {
        // nop
    }
}
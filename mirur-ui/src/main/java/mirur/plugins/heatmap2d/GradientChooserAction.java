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
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.metsci.glimpse.support.colormap.ColorGradient;
import com.metsci.glimpse.support.colormap.ColorGradients;

import mirur.plugin.Icons;
import mirur.plugins.DataPainterImpl.ResetAction;

public abstract class GradientChooserAction extends Action implements IMenuCreator, ResetAction {
	private ColorGradient cur;

	public GradientChooserAction() {
		super("Color Gradient");
		setId(GradientChooserAction.class.getName());
		setMenuCreator(this);
		setToolTipText("Select Color Gradient");
		setImageDescriptor(Icons.getGradient(true));
		setDisabledImageDescriptor(Icons.getGradient(false));

		cur = ColorGradients.jet;
	}

	private void addGradientOption(Menu menu, String name, final ColorGradient gradient, ImageDescriptor icon) {
		Action action = new Action(name, IAction.AS_RADIO_BUTTON) {
			@Override
			public void run() {
				if (isChecked()) {
					cur = gradient;
					select(gradient);
				}
			}
		};
		action.setImageDescriptor(icon);
		action.setChecked(gradient == cur);
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(menu, -1);
	}

	protected abstract void select(ColorGradient gradient);

	@Override
	public void reset() {
		cur = ColorGradients.jet;
		select(cur);
	}

	@Override
	public void validate() {
		// nop
	}

	@Override
	public void dispose() {
	}

	@Override
	public Menu getMenu(Control parent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Menu getMenu(Menu parent) {
		Menu menu = new Menu(parent);
		addGradientOption(menu, "jet", ColorGradients.jet, Icons.getGradient(ColorGradients.jet));
		addGradientOption(menu, "viridis", ColorGradients.viridis, Icons.getGradient(ColorGradients.viridis));
		addGradientOption(menu, "bone", ColorGradients.reverseBone, Icons.getGradient(ColorGradients.reverseBone));
		addGradientOption(menu, "grey", ColorGradients.gray, Icons.getGradient(ColorGradients.gray));
		addGradientOption(menu, "spectral", ColorGradients.spectral, Icons.getGradient(ColorGradients.spectral));

		return menu;
	}
}
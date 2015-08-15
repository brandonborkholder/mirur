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

import static mirur.plugin.Activator.getVariableSelectionModel;
import static mirur.plugin.Activator.getViewSelectionModel;
import mirur.core.VariableObject;
import mirur.plugin.Activator;
import mirur.plugin.Icons;
import mirur.plugins.MirurView;
import mirur.plugins.MirurViews;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

public class SelectViewAction extends Action implements IMenuCreator {
    public SelectViewAction() {
        setId(SelectViewAction.class.getName());
        setMenuCreator(this);
        setText("Painters");
        setToolTipText("Select Painter");
        setImageDescriptor(Activator.getImageDescriptor(Icons.NEWVIEW_PATH));
    }

    @Override
    public void dispose() {
        // nop
    }

    @Override
    public void runWithEvent(Event event) {
        if (event.widget instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) event.widget;
            Control control = toolItem.getParent();
            Menu menu = getMenu(control);

            Rectangle bounds = toolItem.getBounds();
            Point topLeft = new Point(bounds.x, bounds.y + bounds.height);
            menu.setLocation(control.toDisplay(topLeft));
            menu.setVisible(true);
        }
    }

    @Override
    public Menu getMenu(Control parent) {
        Menu menu = new Menu(parent);

        VariableObject obj = getVariableSelectionModel().getActiveSelected();

        if (obj == null) {
            new ActionContributionItem(new NoVariableSelected()).fill(menu, -1);
        } else {
            MirurView selected = getViewSelectionModel().getActiveSelected();

            for (MirurView plugin : MirurViews.plugins()) {
                if (plugin.supportsData(obj)) {
                    Action action = new ViewRadioButton(plugin);
                    action.setChecked(plugin.equals(selected));
                    ActionContributionItem item = new ActionContributionItem(action);
                    item.fill(menu, -1);
                }
            }
        }

        new Separator().fill(menu, -1);
        ActionContributionItem item = new ActionContributionItem(new RequestNewViewAction());
        item.fill(menu, -1);

        return menu;
    }

    @Override
    public Menu getMenu(Menu parent) {
        throw new UnsupportedOperationException();
    }

    private static final class NoVariableSelected extends Action {
        public NoVariableSelected() {
            super("No painter available");
            setEnabled(false);
        }
    }

    private static final class ViewRadioButton extends Action {
        final MirurView view;

        public ViewRadioButton(MirurView view) {
            super(view.getName(), IAction.AS_RADIO_BUTTON);
            this.view = view;
            setImageDescriptor(view.getIcon());
        }

        @Override
        public void run() {
            getViewSelectionModel().select(view);
        }
    }
}

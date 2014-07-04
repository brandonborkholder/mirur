package mirur.plugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

public abstract class ViewMenuAction extends Action implements IMenuCreator {
    public ViewMenuAction() {
        setId(ViewMenuAction.class.getName());
        setMenuCreator(this);
        setText("Options");
        setToolTipText("Select Painter Options");
        setImageDescriptor(Activator.getImageDescriptor(Icons.CONFIG_PATH));
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
    public void dispose() {
        // nop
    }

    @Override
    public Menu getMenu(Control parent) {
        Menu menu = new Menu(parent);
        getMenu(menu);
        return menu;
    }
}

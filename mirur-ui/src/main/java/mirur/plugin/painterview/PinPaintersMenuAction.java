package mirur.plugin.painterview;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

import com.metsci.glimpse.util.Pair;

import mirur.core.VariableObject;
import mirur.plugin.Icons;
import mirur.plugins.DataPainter;

public abstract class PinPaintersMenuAction extends Action implements IMenuCreator {
    private List<ActionContributionItem> painterItems;

    public PinPaintersMenuAction() {
        setId(PinPaintersMenuAction.class.getName());
        setMenuCreator(this);
        setText("Pinned");
        setToolTipText("Pin Painters");
        setImageDescriptor(Icons.getPin(true));
        setDisabledImageDescriptor(Icons.getPin(false));

        painterItems = new ArrayList<>();
    }

    private void pinCurrent() {
        Pair<VariableObject, DataPainter> cur = getCurrent();
        if (cur != null) {
            Action action = new SelectDataPainterAction(cur.first(), cur.second());
            painterItems.add(new ActionContributionItem(action));
        }
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

    @Override
    public Menu getMenu(Menu menu) {
        new ActionContributionItem(new PinCurrentAction()).fill(menu, -1);
        for (ActionContributionItem i : painterItems) {
            i.fill(menu, -1);
        }

        return menu;
    }

    protected abstract Pair<VariableObject, DataPainter> getCurrent();

    protected abstract void select(DataPainter painter);

    private class PinCurrentAction extends Action {
        public PinCurrentAction() {
            setText("Pin Current");
        }

        @Override
        public void run() {
            pinCurrent();
        }
    }

    private class SelectDataPainterAction extends Action {
        private final DataPainter painter;

        public SelectDataPainterAction(VariableObject obj, DataPainter painter) {
            this.painter = painter;
            setText(obj.getName() + " @ " + LocalTime.now().toString());
        }

        @Override
        public void run() {
            select(painter);
        }
    }
}

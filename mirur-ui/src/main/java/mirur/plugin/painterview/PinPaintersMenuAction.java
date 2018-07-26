package mirur.plugin.painterview;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

import com.metsci.glimpse.util.Pair;

import mirur.core.VariableObject;
import mirur.plugin.Icons;
import mirur.plugin.VarObjectSelectListener;
import mirur.plugins.DataPainter;

public abstract class PinPaintersMenuAction extends Action implements IMenuCreator, VarObjectSelectListener {
    private List<SelectDataPainterAction> painterItems;
    private boolean allowPinCurrent;

    public PinPaintersMenuAction() {
        setId(PinPaintersMenuAction.class.getName());
        setMenuCreator(this);
        setText("Pinned");
        setToolTipText("Pin Painters");
        setImageDescriptor(Icons.getPin(true));
        setDisabledImageDescriptor(Icons.getPin(false));

        painterItems = new ArrayList<>();
        allowPinCurrent = false;
    }

    private void pinCurrent() {
        Pair<VariableObject, DataPainter> cur = getCurrent();
        if (cur != null) {
            painterItems.add(new SelectDataPainterAction(cur.first(), cur.second()));
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
        if (allowPinCurrent) {
            new ActionContributionItem(new PinCurrentAction()).fill(menu, -1);
        }

        if (allowPinCurrent && painterItems.size() > 0) {
            new Separator().fill(menu, -1);
        }

        for (Action a : painterItems) {
            ActionContributionItem item = new ActionContributionItem(a);
            item.fill(menu, -1);
        }

        return menu;
    }

    protected abstract Pair<VariableObject, DataPainter> getCurrent();

    protected abstract void select(VariableObject obj, DataPainter painter);

    @Override
    public void variableSelected(VariableObject array) {
        allowPinCurrent = true;
        if (array == null) {
            allowPinCurrent = false;
        }
        for (SelectDataPainterAction p : painterItems) {
            if (p.obj == array) {
                allowPinCurrent = false;
            }
        }
    }

    @Override
    public void clearVariableCacheData() {
        // nop
    }

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
        private final VariableObject obj;
        private final DataPainter painter;

        public SelectDataPainterAction(VariableObject obj, DataPainter painter) {
            this.obj = obj;
            this.painter = painter;
            setText(obj.getName() + " @ " + LocalTime.now().toString());
        }

        @Override
        public void run() {
            select(obj, painter);
        }
    }
}

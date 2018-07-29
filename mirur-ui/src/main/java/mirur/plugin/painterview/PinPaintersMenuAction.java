package mirur.plugin.painterview;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

import mirur.core.VariableObject;
import mirur.plugin.Activator;
import mirur.plugin.Icons;
import mirur.plugin.VarObjectSelectListener;

public abstract class PinPaintersMenuAction extends Action implements IMenuCreator, VarObjectSelectListener {
    private List<SelectDataPainterAction> painterItems;
    private VariableObject selected;

    private boolean doListenToVariablesView;

    public PinPaintersMenuAction() {
        setId(PinPaintersMenuAction.class.getName());
        setMenuCreator(this);
        setText("Pinned");
        setToolTipText("Pin Painters");
        setImageDescriptor(Icons.getPin(true));
        setDisabledImageDescriptor(Icons.getPin(false));

        painterItems = new ArrayList<>();

        variableSelected(null);
        doListenToVariablesView = true;
    }

    private boolean doAllowPinCurrent() {
        boolean allow = true;
        if (selected == null) {
            allow = false;
        }

        for (SelectDataPainterAction p : painterItems) {
            if (p.obj == selected) {
                allow = false;
            }
        }

        return allow;
    }

    protected abstract void pinCurrent();

    protected abstract void select(VariableObject obj);

    private void doSelect(VariableObject obj) {
        selected = obj;
        select(obj);
    }

    protected void add(VariableObject obj) {
        painterItems.add(new SelectDataPainterAction(obj));
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
        new ActionContributionItem(new ListenToVariablesViewAction()).fill(menu, -1);

        if (doAllowPinCurrent()) {
            new ActionContributionItem(new PinCurrentAction()).fill(menu, -1);
        }

        if (painterItems.size() > 0) {
            new Separator().fill(menu, -1);

            for (SelectDataPainterAction a : painterItems) {
                a.updateUi();
                ActionContributionItem item = new ActionContributionItem(a);
                item.fill(menu, -1);
            }

            new ActionContributionItem(new ClearPinnedAction()).fill(menu, -1);
        }

        return menu;
    }

    @Override
    public void variableSelected(VariableObject array) {
        if (!doListenToVariablesView) {
            return;
        }

        doSelect(array);
    }

    @Override
    public void clearVariableCacheData() {
        // nop
    }

    private class PinCurrentAction extends Action {
        public PinCurrentAction() {
            super("Pin Current", IAction.AS_PUSH_BUTTON);
        }

        @Override
        public void run() {
            doListenToVariablesView = false;
            pinCurrent();
        }
    }

    private class SelectDataPainterAction extends Action {
        private final VariableObject obj;
        private final Instant pinTime;

        public SelectDataPainterAction(VariableObject obj) {
            super(obj.getName(), IAction.AS_RADIO_BUTTON);
            this.obj = obj;
            pinTime = Instant.now();
        }

        void updateUi() {
            Duration ago = Duration.between(pinTime, Instant.now());
            long x = ago.toMillis() / 1000;
            long seconds = x % 60;
            x /= 60;
            long minutes = x % 60;
            x /= 60;
            long hours = x % 24;
            x /= 24;
            long days = x;
            String agoStr = null;
            if (days > 0) {
                agoStr = String.format("%,dd%dh%dm%ds ago", days, hours, minutes, seconds);
            } else if (hours > 0) {
                agoStr = String.format("%dh%dm%ds ago", hours, minutes, seconds);
            } else if (minutes > 0) {
                agoStr = String.format("%dm%ds ago", minutes, seconds);
            } else if (seconds > 0) {
                agoStr = String.format("%ds ago", seconds);
            }

            setText(obj.getName() + " @ " + agoStr);
            setChecked(obj == selected);
        }

        @Override
        public void run() {
            doListenToVariablesView = false;
            doSelect(obj);
        }
    }

    private class ListenToVariablesViewAction extends Action {
        public ListenToVariablesViewAction() {
            super("Sync Selection", IAction.AS_CHECK_BOX);
            setId(ListenToVariablesViewAction.class.getName());
            setChecked(doListenToVariablesView);
            setImageDescriptor(Icons.getSync(doListenToVariablesView));
        }

        @Override
        public void run() {
            doListenToVariablesView = isChecked();
            if (doListenToVariablesView) {
                doSelect(Activator.getVariableSelectionModel().getActiveSelected());
            } else {
                doSelect(null);
            }
        }
    }

    private class ClearPinnedAction extends Action {
        public ClearPinnedAction() {
            super("Clear Pinned", IAction.AS_PUSH_BUTTON);
            setId(ClearPinnedAction.class.getName());
            setImageDescriptor(Icons.getDelete());
        }

        @Override
        public void run() {
            doListenToVariablesView = true;
            painterItems.clear();
            doSelect(Activator.getVariableSelectionModel().getActiveSelected());
        }
    }
}

package mirur.plugin;

import mirur.core.PrimitiveArray;
import mirur.plugins.MirurView;
import mirur.plugins.MirurViews;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public abstract class ListDisplaysAction extends Action implements IMenuCreator {
    public ListDisplaysAction() {
        setId(ListDisplaysAction.class.getName());
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
    public Menu getMenu(Control parent) {
        Menu menu = new Menu(parent);

        PrimitiveArray data = getActiveData();

        for (final MirurView plugin : MirurViews.plugins()) {
            Action action = new Action(plugin.getName()) {
                @Override
                public void run() {
                    select(plugin);
                }
            };
            action.setEnabled(data == null || plugin.supportsData(data));
            ActionContributionItem item = new ActionContributionItem(action);
            item.fill(menu, -1);
        }

        return menu;
    }

    @Override
    public Menu getMenu(Menu parent) {
        throw new UnsupportedOperationException();
    }

    private void select(MirurView plugin) {
        setPainter(plugin);
    }

    protected abstract void setPainter(MirurView painter);

    protected abstract PrimitiveArray getActiveData();
}

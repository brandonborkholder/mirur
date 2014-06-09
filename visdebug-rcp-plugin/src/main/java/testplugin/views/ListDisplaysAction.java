package testplugin.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public abstract class ListDisplaysAction extends Action implements IMenuCreator {
    public ListDisplaysAction() {
        setMenuCreator(this);
        setText("Painters");
        setToolTipText("Select Painter");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_CLEAR));
    }

    @Override
    public void dispose() {
        // nop
    }

    @Override
    public Menu getMenu(Control parent) {
        Menu menu = new Menu(parent);

        for (final VisDebugPlugin plugin : VisDebugPlugins.plugins()) {
            Action selectAction = new Action(plugin.getName()) {
                @Override
                public void run() {
                    select(plugin);
                }
            };
            ActionContributionItem item = new ActionContributionItem(selectAction);
            item.fill(menu, -1);
        }

        return menu;
    }

    @Override
    public Menu getMenu(Menu parent) {
        throw new UnsupportedOperationException();
    }

    private void select(VisDebugPlugin plugin) {
        setPainter(plugin);
    }

    protected abstract void setPainter(VisDebugPlugin painter);

    protected abstract PrimitiveArray getActiveData();
}

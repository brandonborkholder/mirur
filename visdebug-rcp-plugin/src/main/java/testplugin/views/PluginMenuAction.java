package testplugin.views;

import mirur.plugin.Activator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public abstract class PluginMenuAction extends Action implements IMenuCreator {
    public PluginMenuAction() {
        setId(PluginMenuAction.class.getName());
        setMenuCreator(this);
        setText("Options");
        setToolTipText("Select Painter Options");
        setImageDescriptor(Activator.getImageDescriptor(Icons.CONFIG_PATH));
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

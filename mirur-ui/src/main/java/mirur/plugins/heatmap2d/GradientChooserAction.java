package mirur.plugins.heatmap2d;

import mirur.plugin.Activator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.metsci.glimpse.support.colormap.ColorGradient;
import com.metsci.glimpse.support.colormap.ColorGradients;

public abstract class GradientChooserAction extends Action implements IMenuCreator {
    public GradientChooserAction() {
        setId(GradientChooserAction.class.getName());
        setMenuCreator(this);
        setText("Color Gradient");
        setToolTipText("Select Color Gradient");
        setImageDescriptor(Activator.getImageDescriptor("icons/gradient_jet.gif"));
    }

    private void addGradientOption(Menu menu, String name, final ColorGradient gradient, ImageDescriptor icon) {
        Action action = new Action(name, icon) {
            @Override
            public void run() {
                select(gradient);
            }
        };

        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(menu, -1);
    }

    protected abstract void select(ColorGradient gradient);

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
        addGradientOption(menu, "jet", ColorGradients.jet, Activator.getImageDescriptor("icons/gradient_jet.gif"));
        addGradientOption(menu, "bathymetry", ColorGradients.bathymetry, Activator.getImageDescriptor("icons/gradient_bathy.gif"));
        addGradientOption(menu, "bone", ColorGradients.reverseBone, Activator.getImageDescriptor("icons/gradient_bone.gif"));
        addGradientOption(menu, "grey", ColorGradients.gray, Activator.getImageDescriptor("icons/gradient_grey.gif"));
        addGradientOption(menu, "topography", ColorGradients.topography, Activator.getImageDescriptor("icons/gradient_topo.gif"));

        return menu;
    }
}
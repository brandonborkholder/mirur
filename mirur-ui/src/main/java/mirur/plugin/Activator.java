package mirur.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
    private static final String PLUGIN_ID = "mirur.mirur-ui";

    private static Activator plugin;

    private static SelectionCache variableCache = new SelectionCache();

    private static SelectionModel selectionModel = new SelectionModel();

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static SelectionCache getVariableCache() {
        return variableCache;
    }

    public static SelectionModel getSelectionModel() {
        return selectionModel;
    }
}

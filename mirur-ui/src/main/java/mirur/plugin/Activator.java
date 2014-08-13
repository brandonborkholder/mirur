package mirur.plugin;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
    public static final String PLUGIN_ID = "mirur.mirur-ui";

    private static Activator plugin;

    private static SelectionCache variableCache;

    private static SelectionModel selectionModel;

    private static Preferences preferences;

    private static RemoteAgentDeployer agentDeployer;

    private static StatisticsCollector statsCollector;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        preferences = new Preferences(InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID));

        PluginLogSupport.initializeLogger();

        statsCollector = new StatisticsCollector();
        agentDeployer = new RemoteAgentDeployer();
        variableCache = new SelectionCache();
        selectionModel = new SelectionModel();

        getStatistics().started();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        preferences.flush();
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

    public static Preferences getPreferences() {
        return preferences;
    }

    public static RemoteAgentDeployer getAgentDeployer() {
        return agentDeployer;
    }

    public static StatisticsCollector getStatistics() {
        return statsCollector;
    }

    public static Image getCachedImage(String name) {
        ImageRegistry registry = getDefault().getImageRegistry();
        Image image = registry.get(name);
        if (image == null) {
            registry.put(name, getImageDescriptor(name));
            image = registry.get(name);
        }

        return image;
    }
}

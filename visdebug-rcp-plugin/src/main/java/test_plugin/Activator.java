package test_plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import testplugin.views.VariableSelectListener;

public class Activator extends AbstractUIPlugin {
    private static final String VARIABLE_VIEW_ID = "org.eclipse.debug.ui.VariableView";

    private static final String PLUGIN_ID = "visdebug-core";

    private static Activator plugin;

    private boolean isVariableSelectInitialized = false;

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

    public void initVariableSelectListener(IViewPart part) {
        if (!isVariableSelectInitialized) {
            part.getViewSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(VARIABLE_VIEW_ID, new VariableSelectListener());
            isVariableSelectInitialized = true;
        }
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}

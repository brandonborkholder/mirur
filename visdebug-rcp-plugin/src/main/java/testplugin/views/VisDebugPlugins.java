package testplugin.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ServiceLoader;

public class VisDebugPlugins {
    private static VisDebugPlugins manager;

    private final Collection<VisDebugPlugin> plugins;

    private VisDebugPlugins() {
        plugins = new ArrayList<>();

        ServiceLoader<VisDebugPlugin> loader = ServiceLoader.load(VisDebugPlugin.class);
        for (VisDebugPlugin plugin : loader) {
            plugins.add(plugin);
        }
    }

    public static Collection<VisDebugPlugin> plugins() {
        if (manager == null) {
            manager = new VisDebugPlugins();
        }

        return Collections.unmodifiableCollection(manager.plugins);
    }
}

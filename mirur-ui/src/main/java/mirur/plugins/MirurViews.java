package mirur.plugins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ServiceLoader;

public class MirurViews {
    private static MirurViews manager;

    private final Collection<MirurView> plugins;

    private MirurViews() {
        plugins = new ArrayList<>();

        ServiceLoader<MirurView> loader = ServiceLoader.load(MirurView.class);
        for (MirurView plugin : loader) {
            plugins.add(plugin);
        }
    }

    public static Collection<MirurView> plugins() {
        if (manager == null) {
            manager = new MirurViews();
        }

        return Collections.unmodifiableCollection(manager.plugins);
    }
}

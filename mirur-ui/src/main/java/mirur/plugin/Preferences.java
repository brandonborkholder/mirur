package mirur.plugin;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

public class Preferences {
    private static final String PREF_SYNC_WITH_VARIABLES_VIEW = "toggle.variables.sync";

    private final IEclipsePreferences prefNode;

    public Preferences(IEclipsePreferences node) {
        this.prefNode = node;
    }

    public boolean doSyncWithVariablesView(String viewId) {
        return prefNode.getBoolean(viewId + "." + PREF_SYNC_WITH_VARIABLES_VIEW, true);
    }

    public void setSyncWithVariablesView(String viewId, boolean newValue) {
        prefNode.putBoolean(viewId + "." + PREF_SYNC_WITH_VARIABLES_VIEW, newValue);
    }

    public void flush() {
        try {
            prefNode.flush();
        } catch (BackingStoreException ex) {
            // TODO log or something
            ex.printStackTrace();
        }
    }
}

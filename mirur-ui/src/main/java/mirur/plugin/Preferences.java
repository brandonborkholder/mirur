package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logSevere;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.osgi.service.prefs.BackingStoreException;

public class Preferences {
    private static final Logger LOGGER = Logger.getLogger(Preferences.class.getName());

    private static final String PREFSUFFIX_SYNC_WITH_VARIABLES_VIEW = "toggle.variables.sync";

    public static final String PREF_SUBMIT_STATISTICS = "submit.statistics";

    private final IEclipsePreferences prefNode;

    private final List<Runnable> changeListeners;

    public Preferences(IEclipsePreferences node) {
        this.prefNode = node;
        changeListeners = new CopyOnWriteArrayList<>();

        prefNode.addPreferenceChangeListener(new IPreferenceChangeListener() {
            @Override
            public void preferenceChange(PreferenceChangeEvent event) {
                for (Runnable l : changeListeners) {
                    l.run();
                }
            }
        });
    }

    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public boolean doSyncWithVariablesView(String viewId) {
        return prefNode.getBoolean(viewId + "." + PREFSUFFIX_SYNC_WITH_VARIABLES_VIEW, true);
    }

    public void setSyncWithVariablesView(String viewId, boolean newValue) {
        prefNode.putBoolean(viewId + "." + PREFSUFFIX_SYNC_WITH_VARIABLES_VIEW, newValue);
    }

    public boolean doSubmitStatistics() {
        return prefNode.getBoolean(PREF_SUBMIT_STATISTICS, true);
    }

    public void setSubmitStatistics(boolean newValue) {
        prefNode.putBoolean(PREF_SUBMIT_STATISTICS, newValue);
    }

    public void flush() {
        try {
            prefNode.flush();
        } catch (BackingStoreException ex) {
            logSevere(LOGGER, "Unexpected error saving preferences", ex);
        }
    }
}

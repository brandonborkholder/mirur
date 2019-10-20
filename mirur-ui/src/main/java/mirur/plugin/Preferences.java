/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logSevere;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.osgi.service.prefs.BackingStoreException;

public class Preferences {
    private static final Logger LOGGER = Logger.getLogger(Preferences.class.getName());

    private static final String PREFSUFFIX_SYNC_WITH_VARIABLES_VIEW = "toggle.variables.sync";

    public static final String ANONYMOUS_ID = "anonid";
    public static final String PREF_MAX_BYTES_TRANSFER = "max.bytes.transfer";

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

        initializeIdIfEmpty();
    }

    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public boolean doSyncWithVariablesView(String viewId) {
        return prefNode.getBoolean(viewId + "." + PREFSUFFIX_SYNC_WITH_VARIABLES_VIEW, true);
    }

    public int getMaxBytesToTransfer() {
        return prefNode.getInt(PREF_MAX_BYTES_TRANSFER, 50 * 1024 * 1024);
    }

    public void setMaxBytesToTransfer(int maxBytes) {
        prefNode.putInt(PREF_MAX_BYTES_TRANSFER, maxBytes);
    }

    public void setSyncWithVariablesView(String viewId, boolean newValue) {
        prefNode.putBoolean(viewId + "." + PREFSUFFIX_SYNC_WITH_VARIABLES_VIEW, newValue);
    }

    public String getAnonymousId() {
        return prefNode.get(ANONYMOUS_ID, "unknown");
    }

    private void initializeIdIfEmpty() {
        if (prefNode.get(ANONYMOUS_ID, null) == null) {
            prefNode.put(ANONYMOUS_ID, UUID.randomUUID().toString());
        }
    }

    public void flush() {
        try {
            prefNode.flush();
        } catch (BackingStoreException ex) {
            logSevere(LOGGER, "Unexpected error saving preferences", ex);
        }
    }
}

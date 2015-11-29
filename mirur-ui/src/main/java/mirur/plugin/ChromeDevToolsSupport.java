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

import org.eclipse.debug.core.model.IVariable;

public class ChromeDevToolsSupport {
    private static final String DEBUG_MODEL_ID = "org.chromium.debug";

    private static final boolean isPluginSupported;

    static {
        boolean success;
        try {
            Class.forName("org.chromium.debug.core.model.Value");
            success = true;
        } catch (Exception ex) {
            success = false;
        }

        isPluginSupported = success;
    }

    public static boolean supports(IVariable var) {
        return var.getModelIdentifier().equals(DEBUG_MODEL_ID) && isPluginSupported;
    }
}

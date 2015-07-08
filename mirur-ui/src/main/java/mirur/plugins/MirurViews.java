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

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

import mirur.core.Array2D;
import mirur.core.PrimitiveArray;

import org.eclipse.jface.resource.ImageDescriptor;

public abstract class SimplePlugin2D implements MirurView {
    private final String name;
    private final ImageDescriptor icon;

    public SimplePlugin2D(String name, ImageDescriptor icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public boolean supportsData(PrimitiveArray array) {
        Class<?> clazz = array.getData().getClass();
        return array instanceof Array2D &&
                (int[][].class.equals(clazz) ||
                        long[][].class.equals(clazz) ||
                        float[][].class.equals(clazz) ||
                        double[][].class.equals(clazz) ||
                        char[][].class.equals(clazz) ||
                        short[][].class.equals(clazz) ||
                        boolean[][].class.equals(clazz));

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageDescriptor getIcon() {
        return icon;
    }
}

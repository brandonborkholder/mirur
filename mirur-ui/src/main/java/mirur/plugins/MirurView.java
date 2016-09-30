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

import mirur.core.VariableObject;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public interface MirurView {
    boolean supportsData(VariableObject obj);

    String getName();

    ImageDescriptor getIcon();

    DataPainter create(GlimpseCanvas canvas, VariableObject obj);
}

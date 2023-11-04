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

import org.eclipse.swt.widgets.Menu;

import com.metsci.glimpse.core.canvas.GlimpseCanvas;
import com.metsci.glimpse.core.layout.GlimpseLayout;

public interface DataPainter {
    GlimpseLayout getLayout();

    void populateConfigMenu(Menu parent);

    void resetAxes();

    void attach(GlimpseCanvas canvas);

    void detach(GlimpseCanvas canvas);

    void dispose(GlimpseCanvas canvas);
}

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
import org.eclipse.swt.widgets.Menu;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public class InvalidPlaceholderView implements MirurView {
    @Override
    public boolean supportsData(VariableObject obj) {
        return true;
    }

    @Override
    public String getName() {
        return "Default show nothing";
    }

    @Override
    public ImageDescriptor getIcon() {
        return null;
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, VariableObject obj) {
        final InvalidPlaceholderLayout layout = new InvalidPlaceholderLayout();
        canvas.addLayout(layout);

        return new DataPainter() {
            @Override
            public GlimpseLayout getLayout() {
                return layout;
            }

            @Override
            public void populateMenu(Menu parent) {
            }

            @Override
            public void resetAxes() {
            }

            @Override
            public void uninstall(GlimpseCanvas canvas) {
                canvas.removeLayout(layout);
            }
        };
    }
}

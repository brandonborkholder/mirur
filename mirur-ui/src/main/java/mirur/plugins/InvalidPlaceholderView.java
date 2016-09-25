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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Menu;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

import mirur.core.VariableObject;

public class InvalidPlaceholderView implements MirurView {
    private InvalidPlaceholderLayout layout;

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
        if (layout == null) {
            layout = new InvalidPlaceholderLayout();
        }

        canvas.addLayout(layout);

        if (obj == null) {
            layout.textPainter.setText(null);
        } else if (obj.getData() instanceof String) {
            layout.textPainter.setText((String) obj.getData());
        } else {
            layout.textPainter.setText(null);
        }

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

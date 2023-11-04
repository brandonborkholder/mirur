/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

import com.metsci.glimpse.core.canvas.GlimpseCanvas;

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
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        if (layout == null) {
            layout = new InvalidPlaceholderLayout();
        }

        if (obj == null) {
            layout.textPainter.setText(null);
        } else if (obj.getData() instanceof String) {
            layout.textPainter.setText((String) obj.getData());
        } else {
            layout.textPainter.setText(null);
        }

        return new DataPainterImpl(layout) {
            @Override
            public void dispose(GlimpseCanvas canvas) {
                // We're going to reuse this layout, so don't dispose
            }
        };
    }
}

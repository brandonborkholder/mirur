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

import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.painter.decoration.BackgroundPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.support.color.GlimpseColor;

public class InvalidPlaceholderLayout extends GlimpseLayout {
    public InvalidPlaceholderLayout() {
        BackgroundPainter backgroundPainter = new BackgroundPainter(true);
        addPainter(backgroundPainter);

        SimpleTextPainter textPainter = new SimpleTextPainter();
        textPainter.setColor(GlimpseColor.getRed());
        textPainter.setFont(18, true);
        textPainter.setText("no valid data or painter selected");
        textPainter.setHorizontalPosition(HorizontalPosition.Center);
        textPainter.setVerticalPosition(VerticalPosition.Center);
        addPainter(textPainter);
    }
}

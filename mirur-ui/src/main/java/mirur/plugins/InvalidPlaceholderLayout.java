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

import static com.metsci.glimpse.painter.decoration.WatermarkPainter.bottomLeft;
import static com.metsci.glimpse.support.color.GlimpseColor.getRed;
import static com.metsci.glimpse.support.font.FontUtils.getDefaultBold;
import static java.lang.Math.max;

import java.awt.image.BufferedImage;

import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.painter.decoration.BackgroundPainter;
import com.metsci.glimpse.painter.decoration.WatermarkPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;

import mirur.plugin.Icons;

public class InvalidPlaceholderLayout extends GlimpseLayout {
    protected SimpleTextPainter textPainter;

    public InvalidPlaceholderLayout() {
        addPainter(new BackgroundPainter(true));

        BufferedImage image = Icons.getMirurLogoImage();
        final double[] minDimensions = { image.getWidth() * 0.4, image.getHeight() * 0.4 };

        WatermarkPainter logoPainter = new WatermarkPainter(image,
                bottomLeft.withMaxAreaFraction(1).withMaxWidthFraction(0.3).withMaxHeightFraction(0.2)) {
            @Override
            protected double[] computeQuadGeometry(double wImage, double hImage, double wBounds, double hBounds) {
                double[] q = super.computeQuadGeometry(wImage, hImage, wBounds, hBounds);
                q[0] = max(q[0], minDimensions[0]);
                q[1] = max(q[1], minDimensions[1]);

                return q;
            }
        };
        addPainter(logoPainter);

        textPainter = new SimpleTextPainter();
        textPainter.setColor(getRed());
        textPainter.setHorizontalPosition(HorizontalPosition.Center);
        textPainter.setVerticalPosition(VerticalPosition.Center);
        textPainter.setPaintBackground(false);
        textPainter.setFont(getDefaultBold(12));
        addPainter(textPainter);
    }
}

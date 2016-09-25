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
import static com.metsci.glimpse.util.logging.LoggerUtils.logWarning;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.painter.decoration.BackgroundPainter;
import com.metsci.glimpse.painter.decoration.WatermarkPainter;

import mirur.plugin.Icons;

public class InvalidPlaceholderLayout extends GlimpseLayout {
    private static final Logger LOGGER = Logger.getLogger(InvalidPlaceholderLayout.class.getName());

    public InvalidPlaceholderLayout() {
        BackgroundPainter backgroundPainter = new BackgroundPainter(true);
        addPainter(backgroundPainter);

        try (InputStream in = InvalidPlaceholderLayout.class.getClassLoader().getResourceAsStream(Icons.MIRUR_LOGO_PATH)) {
            BufferedImage image = ImageIO.read(in);
            WatermarkPainter logoPainter = new WatermarkPainter(image,
                    bottomLeft.withMaxAreaFraction(1).withMaxWidthFraction(0.3).withMaxHeightFraction(0.2));
            addPainter(logoPainter);
        } catch (IOException ex) {
            logWarning(LOGGER, "Failed to load logo image", ex);
        }
    }
}

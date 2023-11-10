/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © 2023 Brandon Borkholder (support@mirur.io)
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
package mirur.plugins.image;

import java.awt.image.BufferedImage;

import com.metsci.glimpse.core.painter.decoration.BackgroundPainter;
import com.metsci.glimpse.core.painter.decoration.BorderPainter;
import com.metsci.glimpse.core.plot.Plot2D;

public class ImagePlot extends Plot2D {
    public ImagePlot(BufferedImage image) {
        initialize();

        setTitleHeight(0);
        setAxisSizeX(0);
        setAxisSizeY(0);
        setAxisSizeZ(0);

        ImagePainter painter = new ImagePainter(image);
        addPainter(new BackgroundPainter(true), Plot2D.BACKGROUND_LAYER);
        getLayoutCenter().addPainter(new BorderPainter(), Plot2D.FOREGROUND_LAYER);
        getLayoutCenter().addPainter(painter, Plot2D.DATA_LAYER);

        getAxisX().setMin(-1);
        getAxisX().setMax(painter.image.getWidth() + 1);
        getAxisY().setMin(-1);
        getAxisY().setMax(painter.image.getHeight() + 1);
        getAxis().lockAspectRatioXY(1);
        getAxis().validate();
    }
}

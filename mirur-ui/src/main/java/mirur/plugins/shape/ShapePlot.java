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
package mirur.plugins.shape;

import java.awt.Shape;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.plot.Plot2D;
import com.metsci.glimpse.plot.SimplePlot2D;

public class ShapePlot extends SimplePlot2D {
    public ShapePlot(Shape shape) {
        setTitleHeight(0);
        setAxisSizeZ(0);

        ShapePainter painter = new ShapePainter(shape);
        addPainter(painter, Plot2D.DATA_LAYER);

        painter.adjustAxis(getAxis());

        padAxis(getAxisX());
        padAxis(getAxisY());
    }

    protected void padAxis(Axis1D axis) {
        double padding = (axis.getMax() - axis.getMin()) * 0.02;
        axis.setMin(axis.getMin() - padding);
        axis.setMax(axis.getMax() + padding);
        axis.validate();
    }
}

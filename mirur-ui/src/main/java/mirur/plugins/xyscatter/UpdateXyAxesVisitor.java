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
package mirur.plugins.xyscatter;

import static com.google.common.primitives.Doubles.isFinite;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.metsci.glimpse.axis.Axis2D;

import mirur.core.AbstractInterleavedVisitor;
import mirur.plugins.DataUnitConverter;

public class UpdateXyAxesVisitor extends AbstractInterleavedVisitor {
    private final DataUnitConverter xUnitConverter;
    private final DataUnitConverter yUnitConverter;

    private double minX = Double.POSITIVE_INFINITY;
    private double maxX = Double.NEGATIVE_INFINITY;
    private double minY = Double.POSITIVE_INFINITY;
    private double maxY = Double.NEGATIVE_INFINITY;

    public UpdateXyAxesVisitor(DataUnitConverter xUnitConverter, DataUnitConverter yUnitConverter) {
        this.xUnitConverter = xUnitConverter;
        this.yUnitConverter = yUnitConverter;
    }

    public void updateAxes(Axis2D axis) {
        double minX = xUnitConverter.data2painter(this.minX);
        double maxX = xUnitConverter.data2painter(this.maxX);
        double minY = yUnitConverter.data2painter(this.minY);
        double maxY = yUnitConverter.data2painter(this.maxY);

        if (minX == maxX) {
            maxX++;
        }

        if (minY == maxY) {
            maxY++;
        }

        axis.getAxisX().setMin(minX);
        axis.getAxisX().setMax(maxX);
        axis.getAxisY().setMin(minY);
        axis.getAxisY().setMax(maxY);
    }

    @Override
    protected void visit(int i, double x, double y) {
        if (isFinite(x)) {
            minX = min(x, minX);
            maxX = max(x, maxX);
        }
        if (isFinite(y)) {
            minY = min(y, minY);
            maxY = max(y, maxY);
        }
    }

    @Override
    protected void visit(int i, float x, float y) {
        visit(i, (double) x, (double) y);
    }
}

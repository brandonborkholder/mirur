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

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.Axis2D;

import mirur.core.MinMaxFiniteValueVisitor;
import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;

public class AxisUtils {
    public static void padAxis2d(Axis2D axis) {
        padAxis(axis.getAxisX());
        padAxis(axis.getAxisY());
    }

    public static void padAxis(Axis1D axis) {
        double padding = (axis.getMax() - axis.getMin()) * 0.02;
        axis.setMin(axis.getMin() - padding);
        axis.setMax(axis.getMax() + padding);
        axis.validate();
    }

    public static void adjustAxisToMinMax(PrimitiveArray array, Axis1D axis, DataUnitConverter unitConverter) {
        MinMaxFiniteValueVisitor minMaxVisitor = VisitArray.visit(array.getData(), new MinMaxFiniteValueVisitor());
        double min = minMaxVisitor.getMin();
        double max = minMaxVisitor.getMax();

        if (min == max) {
            max++;
        }

        axis.setMin(unitConverter.data2painter(min));
        axis.setMax(unitConverter.data2painter(max));
        axis.validate();
    }
}

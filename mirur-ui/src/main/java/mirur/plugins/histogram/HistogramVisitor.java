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
package mirur.plugins.histogram;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

import it.unimi.dsi.fastutil.floats.Float2IntMap;
import it.unimi.dsi.fastutil.floats.Float2IntOpenHashMap;
import mirur.core.AbstractArrayElementVisitor;
import mirur.plugins.DataUnitConverter;

public class HistogramVisitor extends AbstractArrayElementVisitor {
    private final double min;
    private final double max;
    private double step;
    private Float2IntOpenHashMap counts;
    private DataUnitConverter unitConverter;

    public HistogramVisitor(double dataMin, double dataMax, int size, DataUnitConverter unitConverter) {
        this.min = unitConverter.data2painter(dataMin);
        this.max = unitConverter.data2painter(dataMax);
        step = (max - min) / floor(sqrt(size)) * 1.01;
        this.unitConverter = unitConverter;
    }

    public float getMin() {
        return (float) min;
    }

    public float getBinWidth() {
        return (float) step;
    }

    public Float2IntMap getCounts() {
        return counts;
    }

    @Override
    public void visit(double v) {
        if (isInfinite(v) || isNaN(v)) {
            // nop
        } else {
            v = unitConverter.data2painter(v);
            float k = (float) (floor((v - min) / step) * step + min);
            counts.addTo(k, 1);
        }
    }
}

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

import static com.google.common.primitives.Doubles.isFinite;
import static java.lang.Double.isInfinite;
import static java.lang.Math.abs;
import static java.lang.Math.log10;
import static java.lang.Math.max;
import static java.lang.Math.min;

import mirur.core.ArrayElementVisitor;
import mirur.plugins.DataUnitConverter.LinearScaleConverter;

public class ToFloatPrecisionVisitor implements ArrayElementVisitor {
    private double dMin;
    private double dMax;

    public ToFloatPrecisionVisitor() {
        dMin = Double.POSITIVE_INFINITY;
        dMax = Double.NEGATIVE_INFINITY;
    }

    public DataUnitConverter get() {
        double sigfigs = abs(log10(abs(dMax - dMin))) + abs(log10(max(abs(dMax), abs(dMin))));
        if (isInfinite(dMin) || isInfinite(dMax)) {
            return DataUnitConverter.IDENTITY;
        } else if (sigfigs < 7) {
            return DataUnitConverter.IDENTITY;
        } else {
            return new LinearScaleConverter(dMin, dMax);
        }
    }

    @Override
    public void visit(double v) {
        if (isFinite(v)) {
            dMin = min(dMin, v);
            dMax = max(dMax, v);
        }
    }

    @Override
    public void visit(long v) {
        dMin = min(dMin, v);
        dMax = max(dMax, v);
    }

    @Override
    public void visit(float v) {
    }

    @Override
    public void visit(int v) {
    }

    @Override
    public void visit(short v) {
    }

    @Override
    public void visit(char v) {
    }

    @Override
    public void visit(byte v) {
    }

    @Override
    public void visit(boolean v) {
    }
}

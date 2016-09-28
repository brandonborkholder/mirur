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
package mirur.core;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MinMaxFiniteValueVisitor implements ArrayElementVisitor {
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    protected void visitFinite(double v) {
        min = min(min, v);
        max = max(max, v);
    }

    @Override
    public void visit(double v) {
        if (isNaN(v) || isInfinite(v)) {
            return;
        }

        visitFinite(v);
    }

    @Override
    public void visit(long v) {
        visitFinite(v);
    }

    @Override
    public void visit(float v) {
        visit((double) v);
    }

    @Override
    public void visit(int v) {
        visitFinite(v);
    }

    @Override
    public void visit(short v) {
        visitFinite(v);
    }

    @Override
    public void visit(char v) {
        visitFinite(v);
    }

    @Override
    public void visit(byte v) {
        visitFinite(v);
    }

    @Override
    public void visit(boolean v) {
        visitFinite(v ? 1.0 : 0.0);
    }
}

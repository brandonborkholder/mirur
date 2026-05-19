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
package mirur.core;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MinMaxFiniteValueVisitor implements ArrayElementVisitor {
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;
    private int finiteCount = 0;
    private int totalCount = 0;

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public int getFiniteCount() {
        return finiteCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    protected void visitFinite(double v) {
        min = min(min, v);
        max = max(max, v);
        finiteCount++;
    }

    @Override
    public void visit(double v) {
        totalCount++;
        if (isNaN(v) || isInfinite(v)) {
            return;
        }

        visitFinite(v);
    }

    @Override
    public void visit(long v) {
        totalCount++;
        visitFinite(v);
    }

    @Override
    public void visit(float v) {
        totalCount++;
        visit((double) v);
    }

    @Override
    public void visit(int v) {
        totalCount++;
        visitFinite(v);
    }

    @Override
    public void visit(short v) {
        totalCount++;
        visitFinite(v);
    }

    @Override
    public void visit(char v) {
        totalCount++;
        visitFinite(v);
    }

    @Override
    public void visit(byte v) {
        totalCount++;
        visitFinite(v);
    }

    @Override
    public void visit(boolean v) {
        totalCount++;
        visitFinite(v ? 1.0 : 0.0);
    }
}

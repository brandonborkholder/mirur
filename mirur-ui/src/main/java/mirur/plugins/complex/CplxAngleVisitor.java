/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
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
package mirur.plugins.complex;

import static java.lang.Double.isNaN;

import mirur.core.AbstractInterleavedVisitor;

public class CplxAngleVisitor extends AbstractInterleavedVisitor {
    private static final double twopi = 2 * Math.PI;

    private double dest[];
    private double last;

    public double[] get() {
        return dest;
    }

    @Override
    protected void start(int length) {
        dest = new double[length >> 1];
        last = Double.NaN;
    }

    @Override
    protected void visit(int i, double re, double im) {
        double th = Math.atan2(im, re);
        if (isNaN(last)) {
            last = th;
        } else {
            while (last - th > Math.PI) {
                th += twopi;
            }
        }

        last = th;
        dest[i >> 1] = th;
    }

    @Override
    protected void visit(int i, float re, float im) {
        visit(i, (double) re, (double) im);
    }
}

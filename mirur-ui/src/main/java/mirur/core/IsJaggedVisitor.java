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
package mirur.core;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class IsJaggedVisitor extends AbstractArrayVisitor {
    private int minSize1;
    private int maxSize1;

    public IsJaggedVisitor() {
        minSize1 = Integer.MAX_VALUE;
        maxSize1 = Integer.MIN_VALUE;
    }

    public boolean isJagged() {
        return minSize1 != maxSize1;
    }

    @Override
    public void visit(int i, double[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, long[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, float[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, int[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, short[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, char[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, byte[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, boolean[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, int j, double v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, long v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, float v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, short v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, char v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, byte v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, boolean v) {
        throw new UnsupportedOperationException();
    }
}

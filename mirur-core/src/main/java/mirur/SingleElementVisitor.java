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

public class SingleElementVisitor extends ArrayEachElementVisitor {
    private final int I;
    private final int J;

    public SingleElementVisitor(int i, int j, ArrayElementVisitor visitor) {
        super(visitor);
        I = i;
        J = j;
    }

    @Override
    public void visit(double[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, double[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(float[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, float[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(long[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, long[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(int[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, int[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(short[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, short[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(byte[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, byte[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(char[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, char[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(boolean[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, boolean[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }
}

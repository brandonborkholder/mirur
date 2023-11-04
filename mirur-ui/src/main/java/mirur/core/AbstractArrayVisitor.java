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

public abstract class AbstractArrayVisitor implements Array1dVisitor, Array2dVisitor {
    @Override
    public void visit(double[] v) {
        visit(0, v);
    }

    @Override
    public void visit(long[] v) {
        visit(0, v);
    }

    @Override
    public void visit(float[] v) {
        visit(0, v);
    }

    @Override
    public void visit(int[] v) {
        visit(0, v);
    }

    @Override
    public void visit(short[] v) {
        visit(0, v);
    }

    @Override
    public void visit(char[] v) {
        visit(0, v);
    }

    @Override
    public void visit(byte[] v) {
        visit(0, v);
    }

    @Override
    public void visit(boolean[] v) {
        visit(0, v);
    }

    @Override
    public void visit(double[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(long[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(float[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(int[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(short[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(char[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(byte[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(boolean[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    public void visit(int i, double[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, long[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, float[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, int[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, short[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, char[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, byte[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, boolean[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public abstract void visit(int i, int j, double v);

    public abstract void visit(int i, int j, long v);

    public abstract void visit(int i, int j, float v);

    public abstract void visit(int i, int j, int v);

    public abstract void visit(int i, int j, short v);

    public abstract void visit(int i, int j, char v);

    public abstract void visit(int i, int j, byte v);

    public abstract void visit(int i, int j, boolean v);
}

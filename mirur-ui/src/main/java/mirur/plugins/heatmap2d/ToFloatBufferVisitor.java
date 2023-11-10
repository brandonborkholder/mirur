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
package mirur.plugins.heatmap2d;

import java.nio.FloatBuffer;

import mirur.core.Array2dVisitor;

public class ToFloatBufferVisitor implements Array2dVisitor {
    private final FloatBuffer buf;

    public ToFloatBufferVisitor(FloatBuffer buffer) {
        buf = buffer;
    }

    private void add(float v) {
        buf.put(v);
    }

    @Override
    public void visit(double[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add((float) v[i][j]);
            }
        }
    }

    @Override
    public void visit(long[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(float[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(int[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(short[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(char[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(byte[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(boolean[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j] ? 1 : 0);
            }
        }
    }
}

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

public abstract class AbstractInterleavedVisitor implements Array1dVisitor {
    protected void start(int length) {
    }

    protected void stop() {
    }

    @Override
    public void visit(double[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(long[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(float[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(int[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(short[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(char[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(byte[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(boolean[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i] ? 1 : 0, v[i + 1] ? 1 : 0);
        }
        stop();
    }

    protected abstract void visit(int i, double v1, double v2);

    protected abstract void visit(int i, float v1, float v2);
}

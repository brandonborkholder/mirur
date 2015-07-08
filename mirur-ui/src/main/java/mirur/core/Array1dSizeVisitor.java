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

public class Array1dSizeVisitor implements Array1dVisitor {
    private int size;

    public int getSize() {
        return size;
    }

    @Override
    public void visit(double[] v) {
        size = v.length;
    }

    @Override
    public void visit(long[] v) {
        size = v.length;
    }

    @Override
    public void visit(float[] v) {
        size = v.length;
    }

    @Override
    public void visit(int[] v) {
        size = v.length;
    }

    @Override
    public void visit(short[] v) {
        size = v.length;
    }

    @Override
    public void visit(char[] v) {
        size = v.length;
    }

    @Override
    public void visit(byte[] v) {
        size = v.length;
    }

    @Override
    public void visit(boolean[] v) {
        size = v.length;
    }
}

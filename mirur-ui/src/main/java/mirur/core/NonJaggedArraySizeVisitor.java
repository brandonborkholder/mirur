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

public class NonJaggedArraySizeVisitor implements Array2dVisitor {
    private int size0;
    private int size1;

    public int getSize0() {
        return size0;
    }

    public int getSize1() {
        return size1;
    }

    @Override
    public void visit(double[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(long[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(float[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(int[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(short[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(char[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(byte[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(boolean[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }
}

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

public class ElementToStringVisitor implements ArrayElementVisitor {
    private String text;

    @Override
    public void visit(double v) {
        text = Double.toString(v);
    }

    @Override
    public void visit(long v) {
        text = Long.toString(v);
    }

    @Override
    public void visit(float v) {
        text = Float.toString(v);
    }

    @Override
    public void visit(int v) {
        text = Integer.toString(v);
    }

    @Override
    public void visit(short v) {
        text = Short.toString(v);
    }

    @Override
    public void visit(char v) {
        text = Short.toString((short) v);
    }

    @Override
    public void visit(byte v) {
        text = Byte.toString(v);
    }

    @Override
    public void visit(boolean v) {
        text = Boolean.toString(v);
    }

    public String getText() {
        return text;
    }
}

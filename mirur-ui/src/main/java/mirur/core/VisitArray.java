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

import static mirur.core.PrimitiveTest.isPrimitiveArray1d;
import static mirur.core.PrimitiveTest.isPrimitiveArray2d;

public class VisitArray {
    public static <T extends ArrayElementVisitor> T visit(Object array, T visitor, int index) {
        if (isPrimitiveArray1d(array.getClass())) {
            SingleElementVisitor visitor2 = new SingleElementVisitor(0, index, visitor);
            visit1d(array, visitor2);
            return visitor;
        } else {
            throw new AssertionError();
        }
    }

    public static <T extends ArrayElementVisitor> T visit(Object array, T visitor, int i, int j) {
        if (isPrimitiveArray2d(array.getClass())) {
            SingleElementVisitor visitor2 = new SingleElementVisitor(i, j, visitor);
            visit2d(array, visitor2);
            return visitor;
        } else {
            throw new AssertionError();
        }
    }

    public static <T extends Array1dVisitor> T visit1d(Object array, T visitor) {
        if (isPrimitiveArray1d(array.getClass())) {
            if (array instanceof double[]) {
                visitor.visit((double[]) array);
            } else if (array instanceof long[]) {
                visitor.visit((long[]) array);
            } else if (array instanceof float[]) {
                visitor.visit((float[]) array);
            } else if (array instanceof int[]) {
                visitor.visit((int[]) array);
            } else if (array instanceof short[]) {
                visitor.visit((short[]) array);
            } else if (array instanceof char[]) {
                visitor.visit((char[]) array);
            } else if (array instanceof byte[]) {
                visitor.visit((byte[]) array);
            } else if (array instanceof boolean[]) {
                visitor.visit((boolean[]) array);
            } else {
                throw new AssertionError("forgot something?");
            }
        } else {
            throw new AssertionError();
        }

        return visitor;
    }

    public static <T extends Array2dVisitor> T visit2d(Object array, T visitor) {
        if (isPrimitiveArray2d(array.getClass())) {
            if (array instanceof double[][]) {
                visitor.visit((double[][]) array);
            } else if (array instanceof long[][]) {
                visitor.visit((long[][]) array);
            } else if (array instanceof float[][]) {
                visitor.visit((float[][]) array);
            } else if (array instanceof int[][]) {
                visitor.visit((int[][]) array);
            } else if (array instanceof short[][]) {
                visitor.visit((short[][]) array);
            } else if (array instanceof char[][]) {
                visitor.visit((char[][]) array);
            } else if (array instanceof byte[][]) {
                visitor.visit((byte[][]) array);
            } else if (array instanceof boolean[][]) {
                visitor.visit((boolean[][]) array);
            } else {
                throw new AssertionError("forgot something?");
            }
        } else {
            throw new AssertionError();
        }

        return visitor;
    }

    public static <T extends ArrayElementVisitor> T visit(Object array, T visitor) {
        if (isPrimitiveArray1d(array.getClass())) {
            Array1dVisitor visitor2 = new ArrayEachElementVisitor(visitor);
            visit1d(array, visitor2);
            return visitor;
        } else if (isPrimitiveArray2d(array.getClass())) {
            Array2dVisitor visitor2 = new ArrayEachElementVisitor(visitor);
            visit2d(array, visitor2);
            return visitor;
        } else {
            throw new AssertionError();
        }
    }
}

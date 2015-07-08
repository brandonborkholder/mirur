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

public class PrimitiveTest {
    public static boolean isPrimitiveArray1d(Class<?> clazz) {
        return clazz.isArray() && isPrimitive(clazz.getComponentType());
    }

    public static boolean isPrimitiveArray2d(Class<?> clazz) {
        return clazz.isArray() && isPrimitiveArray1d(clazz.getComponentType());
    }

    public static boolean isPrimitiveArray(Class<?> clazz) {
        return clazz.isArray() &&
                (isPrimitive(clazz.getComponentType()) || isPrimitiveArray(clazz.getComponentType()));
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive();
    }

    public static Class<?> getPrimitiveComponent(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return clazz;
        } else if (clazz.isArray()) {
            return getPrimitiveComponent(clazz.getComponentType());
        } else {
            throw new IllegalArgumentException(clazz.getName() + " is not a primitive array");
        }
    }

    public static boolean isPrimitiveName(String typeName) {
        switch (typeName.toLowerCase()) {
        case "double":
        case "long":
        case "float":
        case "int":
        case "short":
        case "char":
        case "byte":
        case "boolean":
            return true;

        default:
            return false;
        }
    }
}

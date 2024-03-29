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

public class Array2DRectangular implements Array2D {
    private final String name;
    private final Object orig;

    private final int size0;
    private final int size1;

    public Array2DRectangular(String name, Object array) {
        this.name = name;
        this.orig = array;

        NonJaggedArraySizeVisitor sizeVisitor = VisitArray.visit2d(array, new NonJaggedArraySizeVisitor());
        size0 = sizeVisitor.getSize0();
        size1 = sizeVisitor.getSize1();
    }

    @Override
    public String getSignature() {
        return orig.getClass().getSimpleName();
    }

    @Override
    public boolean isJagged() {
        return false;
    }

    @Override
    public Object getData() {
        return orig;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumDimensions() {
        return 2;
    }

    @Override
    public int getMaxSize(int dimension) {
        if (dimension == 0) {
            return size0;
        } else if (dimension == 1) {
            return size1;
        } else {
            throw new AssertionError("No dimension: " + dimension);
        }
    }

    @Override
    public String toString() {
        return String.format("%s[%d][%d]", orig.getClass().getComponentType().getComponentType(), size0, size1);
    }
}

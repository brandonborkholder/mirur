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
package mirur.plugins.complex;

import com.metsci.glimpse.core.axis.Axis1D;

import mirur.core.ElementToStringVisitor;
import mirur.core.VisitArray;
import mirur.plugins.Array1DTitlePainter;

public class Complex1DTitlePainter extends Array1DTitlePainter {
    public Complex1DTitlePainter(Axis1D srcAxis) {
        super(srcAxis);
    }

    @Override
    protected String format(int index) {
        String realStr = VisitArray.visit(array.getData(), new ElementToStringVisitor(), index * 2).getText();
        String imagStr = VisitArray.visit(array.getData(), new ElementToStringVisitor(), index * 2 + 1).getText();

        if (realStr == null || imagStr == null) {
            return String.format("%s[%d] %s", array.getData().getClass().getComponentType(), array.getSize(), array.getName());
        } else {
            return String.format("%s[%d,%d] = %s + %si", array.getName(), index * 2, index * 2 + 1, realStr, imagStr);
        }
    }
}

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
package mirur.plugins.complex;

import com.metsci.glimpse.core.axis.Axis1D;

import mirur.core.Array1D;
import mirur.core.ElementToStringVisitor;
import mirur.core.VisitArray;
import mirur.plugins.Array1DTitlePainter;

public class AngleMagnitude1DTitlePainter extends Array1DTitlePainter {
    private Array1D angle;
    private Array1D magnitude;

    public AngleMagnitude1DTitlePainter(Axis1D srcAxis) {
        super(srcAxis);
    }

    public void setArrays(Array1D angle, Array1D magnitude) {
        super.setArray(angle);

        this.angle = angle;
        this.magnitude = magnitude;
    }

    @Override
    protected String format(int index) {
        String angStr = VisitArray.visit(angle.getData(), new ElementToStringVisitor(), index).getText();
        String magStr = VisitArray.visit(magnitude.getData(), new ElementToStringVisitor(), index).getText();

        if (angStr == null || magStr == null) {
            return "";
        } else {
            return String.format("\u0398 = %s rad, magnitude = %s", angStr, magStr);
        }
    }
}

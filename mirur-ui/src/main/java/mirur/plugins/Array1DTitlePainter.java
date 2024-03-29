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
package mirur.plugins;

import com.metsci.glimpse.core.axis.Axis1D;
import com.metsci.glimpse.core.context.GlimpseContext;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter;

import mirur.core.Array1D;
import mirur.core.ElementToStringVisitor;
import mirur.core.VisitArray;

public class Array1DTitlePainter extends SimpleTextPainter {
    protected final Axis1D srcAxis;
    protected Array1D array;
    protected int lastIndex;

    protected int[] indexMap;

    public Array1DTitlePainter(Axis1D srcAxis) {
        this.srcAxis = srcAxis;
    }

    public void setArray(Array1D array) {
        this.array = array;
    }

    public void setIndexMap(int[] map) {
        indexMap = map;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        if (array == null) {
            return;
        }

        double selected = srcAxis.getSelectionCenter();
        int axisIdx = (int) Math.round(selected);
        if (axisIdx != lastIndex) {
            int arrayIdx = axisIdx;
            if (indexMap != null && 0 <= axisIdx && axisIdx < indexMap.length) {
                arrayIdx = indexMap[axisIdx];
            }
            text = format(arrayIdx);
            lastIndex = axisIdx;
            setText(text);
        }

        super.paintTo(context);
    }

    protected String format(int index) {
        String value = VisitArray.visit(array.getData(), new ElementToStringVisitor(), index).getText();
        if (value == null) {
            return String.format("%s[%d] %s", array.getData().getClass().getComponentType(), array.getSize(), array.getName());
        } else {
            return String.format("%s[%d] = %s", array.getName(), index, value);
        }
    }
}

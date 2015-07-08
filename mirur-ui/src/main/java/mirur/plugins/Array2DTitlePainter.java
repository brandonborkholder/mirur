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

import mirur.core.Array2D;
import mirur.core.ElementToStringVisitor;
import mirur.core.VisitArray;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.info.SimpleTextPainter;

public class Array2DTitlePainter extends SimpleTextPainter {
    private final Axis2D srcAxis;
    private Array2D array;
    private int lastI;
    private int lastJ;

    public Array2DTitlePainter(Axis2D srcAxis) {
        this.srcAxis = srcAxis;
    }

    public void setArray(Array2D array) {
        this.array = array;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        if (array == null) {
            return;
        }

        double selectedX = srcAxis.getAxisX().getSelectionCenter();
        double selectedY = srcAxis.getAxisY().getSelectionCenter();
        int i = (int) Math.floor(selectedX);
        int j = (int) Math.floor(selectedY);
        if (i != lastI || j != lastJ) {
            String text = format(i, j);
            lastI = i;
            lastJ = j;
            setText(text);
        }

        super.paintTo(context);
    }

    private String format(int i, int j) {
        String value = VisitArray.visit(array.getData(), new ElementToStringVisitor(), i, j).getText();
        if (value == null) {
            Class<?> innerComponent = array.getData().getClass().getComponentType().getComponentType();
            return String.format("%s[%d][%s] %s", innerComponent, array.getMaxSize(0), array.getMaxSize(1), array.getName());
        } else {
            return String.format("%s[%d][%d] = %s", array.getName(), i, j, value);
        }
    }
}

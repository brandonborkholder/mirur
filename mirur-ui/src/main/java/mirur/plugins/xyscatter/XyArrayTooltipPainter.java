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
package mirur.plugins.xyscatter;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.info.TooltipPainter;

import it.unimi.dsi.fastutil.ints.IntList;
import mirur.core.Array1D;

public class XyArrayTooltipPainter extends TooltipPainter {
    protected final Axis2D srcAxis;
    protected final Array1D array;
    protected final XyPointIndex index;
    protected double lastSelectedX;
    protected double lastSelectedY;

    public XyArrayTooltipPainter(Axis2D srcAxis, XyPointIndex index, Array1D array) {
        this.srcAxis = srcAxis;
        this.index = index;
        this.array = array;

        lastSelectedX = Double.NaN;
        lastSelectedY = Double.NaN;

        setBreakOnEol(true);
    }

    @Override
    public void paintTo(GlimpseContext context) {
        if (array == null) {
            setVisible(false);
            return;
        }

        double selectedX = srcAxis.getAxisX().getSelectionCenter();
        double selectedY = srcAxis.getAxisY().getSelectionCenter();
        if (lastSelectedX != selectedX || lastSelectedY != selectedY) {
            lastSelectedX = selectedX;
            lastSelectedY = selectedY;
            setLocationAxisCoords(selectedX, selectedY);

            double pxX = 5.0 / srcAxis.getAxisX().getPixelsPerValue();
            double pxY = 5.0 / srcAxis.getAxisY().getPixelsPerValue();
            IntList indexes = index.getArrayIdx(selectedX, selectedY, pxX, pxY);
            String text = format(indexes);
            setText(text);

            setVisible(!indexes.isEmpty());
        }

        super.paintTo(context);
    }

    protected String format(IntList indexes) {
        StringBuilder sb = new StringBuilder();
        int maxNum = 5;
        for (int i = 0; i < indexes.size() && i < maxNum; i++) {
            if (sb.length() > 0) {
                sb.append("\n");
            }

            int idx = indexes.get(i);
            sb.append("[");
            sb.append(idx * 2);
            sb.append(",");
            sb.append(idx * 2 + 1);
            sb.append("]");
        }

        if (indexes.size() > maxNum) {
            sb.append("\n... ");
            sb.append(String.format("%,d", indexes.size() - maxNum));
            sb.append(" more");
        }

        return sb.toString();
    }
}

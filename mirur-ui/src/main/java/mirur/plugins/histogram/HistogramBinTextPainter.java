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
package mirur.plugins.histogram;

import com.metsci.glimpse.core.axis.Axis2D;
import com.metsci.glimpse.core.context.GlimpseContext;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter;

import mirur.core.PrimitiveArray;
import mirur.plugins.DataUnitConverter;

public class HistogramBinTextPainter extends SimpleTextPainter {
    private final Axis2D srcAxis;
    private HistogramPainter histPainter;
    private PrimitiveArray array;
    private double lastBin;
    private DataUnitConverter unitConverter;

    public HistogramBinTextPainter(Axis2D srcAxis) {
        this.srcAxis = srcAxis;
    }

    public void setHistogramPainter(PrimitiveArray array, HistogramPainter painter, DataUnitConverter unitConverter) {
        this.array = array;
        this.unitConverter = unitConverter;
        histPainter = painter;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        if (array == null) {
            return;
        }

        double selected = srcAxis.getAxisX().getSelectionCenter();
        double bin = histPainter.getBin(selected);
        if (bin != lastBin) {
            int count = histPainter.getCount(bin);
            String text = format(bin, histPainter.getBinSize(), count);
            setText(text);

            lastBin = bin;
        }

        super.paintTo(context);
    }

    private String format(double bin, double binWidth, int count) {
        double binLo = unitConverter.painter2data(bin);
        double binHi = unitConverter.painter2data(bin + binWidth);
        return String.format("%s has %d values in [%s, %s)", array.getName(), count, binLo, binHi);
    }
}

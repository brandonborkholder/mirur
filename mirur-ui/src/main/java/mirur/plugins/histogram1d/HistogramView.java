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
package mirur.plugins.histogram1d;

import mirur.core.Array1D;
import mirur.core.MinMaxFiniteValueVisitor;
import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;
import mirur.plugins.Array1DPlot;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.SimplePlugin1D;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;

public class HistogramView extends SimplePlugin1D {
    public HistogramView() {
        super("Histogram", null);
    }

    @Override
    public boolean supportsData(PrimitiveArray array) {
        Class<?> clazz = array.getData().getClass();
        return super.supportsData(array) && !(boolean[].class.equals(clazz));
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, PrimitiveArray array) {
        final Array1D array1d = (Array1D) array;
        final HistogramPainter painter = createPainter(array1d);
        Array1DPlot plot = new Array1DPlot(painter, array1d) {
            @Override
            protected SimpleTextPainter createTitlePainter() {
                SimpleTextPainter painter = new HistogramBinTextPainter(axis);
                painter.setHorizontalPosition(HorizontalPosition.Left);
                painter.setVerticalPosition(VerticalPosition.Center);
                return painter;
            }

            @Override
            protected void setTitlePainterData(Array1D array) {
                ((HistogramBinTextPainter) titlePainter).setHistogramPainter(array1d, painter);
            }

            @Override
            protected void updateAxesBounds(Array1D array) {
                painter.autoAdjustAxisBounds(axis);
                padAxis(getAxisY());
            }
        };

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());

        finalInstall(canvas, result);
        return result;
    }

    @Override
    protected HistogramPainter createPainter(Array1D array) {
        HistogramPainter painter = new HistogramPainter();
        MinMaxFiniteValueVisitor minmax = VisitArray.visit(array.getData(), new MinMaxFiniteValueVisitor());
        HistogramVisitor hist = VisitArray.visit1d(array.getData(), new HistogramVisitor(minmax.getMin(), minmax.getMax()));
        painter.setData(hist.getCounts(), (float) minmax.getMin(), hist.getBinWidth());
        return painter;
    }
}

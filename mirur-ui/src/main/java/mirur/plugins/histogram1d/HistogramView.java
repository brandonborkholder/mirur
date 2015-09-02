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
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.Array1DPlot;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.MirurView;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;

public class HistogramView implements MirurView {
    @Override
    public String getName() {
        return "Histogram";
    }

    @Override
    public ImageDescriptor getIcon() {
        return null;
    }

    @Override
    public boolean supportsData(VariableObject obj) {
        Class<?> clazz = obj.getData().getClass();
        return int[].class.equals(clazz) ||
                long[].class.equals(clazz) ||
                float[].class.equals(clazz) ||
                double[].class.equals(clazz) ||
                char[].class.equals(clazz) ||
                byte[].class.equals(clazz) ||
                short[].class.equals(clazz);
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, VariableObject obj) {
        final Array1D array1d = (Array1D) obj;
        final HistogramPainter painter = createPainter(array1d);
        Array1DPlot plot = new Array1DPlot(painter, array1d, DataUnitConverter.IDENTITY) {
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
        canvas.addLayout(plot);
        return result;
    }

    protected HistogramPainter createPainter(Array1D array) {
        HistogramPainter painter = new HistogramPainter();
        MinMaxFiniteValueVisitor minmax = VisitArray.visit(array.getData(), new MinMaxFiniteValueVisitor());
        HistogramVisitor hist = VisitArray.visit1d(array.getData(), new HistogramVisitor(minmax.getMin(), minmax.getMax()));
        painter.setData(hist.getCounts(), (float) minmax.getMin(), hist.getBinWidth());
        return painter;
    }
}

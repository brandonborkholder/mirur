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

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.axis.painter.label.GridAxisLabelHandler;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;

import mirur.core.Array1D;
import mirur.core.MinMaxFiniteValueVisitor;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.Array1DPlot;
import mirur.plugins.AxisUtils;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.DataUnitConverter.DataAxisUnitConverter;
import mirur.plugins.HdrAxisLabelHandler;
import mirur.plugins.MirurView;
import mirur.plugins.ToFloatPrecisionVisitor;

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
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        final Array1D array1d = (Array1D) obj;

        MinMaxFiniteValueVisitor minMax = VisitArray.visit(array1d.getData(), new MinMaxFiniteValueVisitor());
        final DataUnitConverter unitConverter = VisitArray.visit(array1d.getData(), new ToFloatPrecisionVisitor()).get();

        HistogramVisitor hist = VisitArray.visit1d(array1d.getData(), new HistogramVisitor(minMax.getMin(), minMax.getMax(), unitConverter));
        final HistogramPainter painter = new HistogramPainter();
        painter.setData(hist.getCounts(), hist.getMin(), hist.getBinWidth());

        Array1DPlot plot = new Array1DPlot(painter, array1d, DataUnitConverter.IDENTITY) {
            {
                painter.autoAdjustAxisBounds(axis);
                AxisUtils.padAxis(getAxisX());
                AxisUtils.padAxis(getAxisY());
            }

            @Override
            protected void initialize() {
                super.initialize();
                setAxisSizeX(35);
            }

            @Override
            protected SimpleTextPainter createTitlePainter() {
                SimpleTextPainter painter = new HistogramBinTextPainter(axis);
                painter.setHorizontalPosition(HorizontalPosition.Left);
                return painter;
            }

            @Override
            protected void setTitlePainterData(Array1D array) {
                ((HistogramBinTextPainter) titlePainter).setHistogramPainter(array1d, painter, unitConverter);
            }

            @Override
            protected GridAxisLabelHandler createLabelHandlerX() {
                return new HdrAxisLabelHandler();
            }

            @Override
            protected GridAxisLabelHandler createLabelHandlerY() {
                return new GridAxisLabelHandler();
            }
        };
        plot.getLabelHandlerX().setAxisUnitConverter(new DataAxisUnitConverter(unitConverter));
        plot.getLabelHandlerX().setTickSpacing(40);

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());
        return result;
    }
}

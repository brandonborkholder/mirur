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
package mirur.plugins.histogram;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.core.canvas.GlimpseCanvas;

import mirur.core.MinMaxFiniteValueVisitor;
import mirur.core.PrimitiveArray;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.DataUnitConverter;
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
                short[].class.equals(clazz) ||
                int[][].class.equals(clazz) ||
                long[][].class.equals(clazz) ||
                float[][].class.equals(clazz) ||
                double[][].class.equals(clazz) ||
                char[][].class.equals(clazz) ||
                byte[][].class.equals(clazz) ||
                short[][].class.equals(clazz);
    }

    @Override
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        PrimitiveArray array = (PrimitiveArray) obj;

        MinMaxFiniteValueVisitor minMax = VisitArray.visit(array.getData(), new MinMaxFiniteValueVisitor());
        DataUnitConverter unitConverter = ToFloatPrecisionVisitor.create(minMax.getMin(), minMax.getMax());

        HistogramVisitor histVisitor = new HistogramVisitor(minMax.getMin(), minMax.getMax(), minMax.getFiniteCount(), unitConverter);
        HistogramVisitor hist = VisitArray.visit(array.getData(), histVisitor);
        HistogramPainter painter = new HistogramPainter(hist.getCounts(), hist.getMin(), hist.getBinWidth());
        HistogramPlot plot = new HistogramPlot(painter, array, unitConverter);

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());
        return result;
    }
}

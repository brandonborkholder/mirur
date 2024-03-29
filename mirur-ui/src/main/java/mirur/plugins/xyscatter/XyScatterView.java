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
package mirur.plugins.xyscatter;

import static com.metsci.glimpse.core.support.font.FontUtils.getDefaultBold;

import com.metsci.glimpse.core.axis.painter.label.GridAxisLabelHandler;
import com.metsci.glimpse.core.canvas.GlimpseCanvas;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.core.plot.Plot2D;
import com.metsci.glimpse.core.plot.SimplePlot2D;

import mirur.core.Array1D;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.AxisUtils;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.DataUnitConverter.DataAxisUnitConverter;
import mirur.plugins.HdrAxisLabelHandler;
import mirur.plugins.SimplePlugin2D;
import mirur.plugins.ToFloatPrecisionVisitor;

public class XyScatterView extends SimplePlugin2D {
    public XyScatterView() {
        super("xy Scatter", null);
    }

    @Override
    public boolean supportsData(VariableObject obj) {
        if (obj instanceof Array1D && ((Array1D) obj).getSize() % 2 == 0) {
            Class<?> clazz = ((Array1D) obj).getData().getClass();
            return int[].class.equals(clazz) ||
                    long[].class.equals(clazz) ||
                    float[].class.equals(clazz) ||
                    double[].class.equals(clazz) ||
                    char[].class.equals(clazz) ||
                    short[].class.equals(clazz);
        } else {
            return false;
        }
    }

    @Override
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        Array1D array = (Array1D) obj;

        XyMinMaxFiniteValueVisitor xyVisitor = VisitArray.visit1d(array.getData(), new XyMinMaxFiniteValueVisitor());
        DataUnitConverter xUnitConverter = ToFloatPrecisionVisitor.create(xyVisitor.xVisitor.getMin(), xyVisitor.xVisitor.getMax());
        DataUnitConverter yUnitConverter = ToFloatPrecisionVisitor.create(xyVisitor.yVisitor.getMin(), xyVisitor.yVisitor.getMax());
        XyPointIndex index = new XyPointIndex(xUnitConverter, yUnitConverter);
        VisitArray.visit1d(array.getData(), index);

        SimplePlot2D plot = new SimplePlot2D() {
            @Override
            protected void initialize() {
                super.initialize();

                titlePainter.setHorizontalPosition(HorizontalPosition.Left);
                titlePainter.setVerticalPosition(VerticalPosition.Center);
                setTitleHeight(30);
                setAxisSizeX(35);
                setAxisSizeY(65);
                setBorderSize(5);
                getLabelHandlerX().setTickSpacing(40);
                getLabelHandlerY().setTickSpacing(40);
                setTitleFont(getDefaultBold(14));
            }

            @Override
            protected void updatePainterLayout() {
                super.updatePainterLayout();
                getLayoutManager().setLayoutConstraints(
                        String.format("bottomtotop, gapx 0, gapy 0, insets %d %d %d %d", 0, outerBorder, outerBorder, outerBorder));
                titleLayout.setLayoutData(String.format("cell 0 0 2 1, growx, height %d!", titleSpacing));
                invalidateLayout();
            }

            @Override
            protected GridAxisLabelHandler createLabelHandlerX() {
                return new HdrAxisLabelHandler();
            }

            @Override
            protected GridAxisLabelHandler createLabelHandlerY() {
                return new HdrAxisLabelHandler();
            }
        };

        plot.setTitle(String.format("%s %s[%d]", array.getSignature(), array.getName(), array.getSize()));
        plot.getLabelHandlerX().setAxisUnitConverter(new DataAxisUnitConverter(xUnitConverter));
        plot.getLabelHandlerY().setAxisUnitConverter(new DataAxisUnitConverter(yUnitConverter));

        XyArrayTooltipPainter tooltipPainter = new XyArrayTooltipPainter(plot.getAxis(), index, array);
        plot.getLayoutCenter().addPainter(tooltipPainter, Plot2D.FOREGROUND_LAYER);

        XyPointsPainter pointsPainter = new XyPointsPainter(array, xUnitConverter, yUnitConverter);
        plot.getLayoutCenter().addPainter(pointsPainter, Plot2D.DATA_LAYER);

        plot.getAxisX().addAxisListener(new FixedPixelSelectionSizeListener(10));
        plot.getAxisY().addAxisListener(new FixedPixelSelectionSizeListener(10));

        AxisUtils.adjustAxisToMinMax(xyVisitor.xVisitor.getMin(), xyVisitor.xVisitor.getMax(), plot.getAxisX(), xUnitConverter);
        AxisUtils.adjustAxisToMinMax(xyVisitor.yVisitor.getMin(), xyVisitor.yVisitor.getMax(), plot.getAxisY(), yUnitConverter);
        AxisUtils.padAxis2d(plot.getAxis());
        plot.getAxis().validate();

        DataPainterImpl painter = new DataPainterImpl(plot);
        painter.addAxis(plot.getAxis());
        painter.addAction(new ShowAsDensityAction(pointsPainter));

        return painter;
    }
}

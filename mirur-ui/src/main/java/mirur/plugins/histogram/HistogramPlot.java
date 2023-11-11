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

import static com.metsci.glimpse.core.support.font.FontUtils.getDefaultBold;

import com.metsci.glimpse.core.axis.painter.label.GridAxisLabelHandler;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.core.plot.SimplePlot2D;

import mirur.core.PrimitiveArray;
import mirur.plugins.AxisUtils;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.DataUnitConverter.DataAxisUnitConverter;
import mirur.plugins.HdrAxisLabelHandler;

public class HistogramPlot extends SimplePlot2D {
    public HistogramPlot(HistogramPainter dataPainter, PrimitiveArray array, DataUnitConverter unitConverter) {
        getLabelHandlerX().setAxisUnitConverter(new DataAxisUnitConverter(unitConverter));
        getLabelHandlerX().setTickSpacing(40);

        dataPainter.autoAdjustAxisBounds(axis);
        AxisUtils.padAxis(getAxisX());
        AxisUtils.padAxis(getAxisY());

        ((HistogramBinTextPainter) titlePainter).setHistogramPainter(array, dataPainter, unitConverter);

        setTitleFont(getDefaultBold(14));
        addPainter(dataPainter, DATA_LAYER);
    }

    @Override
    protected void initializePainters() {
        super.initializePainters();

        getCrosshairPainter().showSelectionBox(false);
        titlePainter.setHorizontalPosition(HorizontalPosition.Left);
    }

    @Override
    protected void initialize() {
        super.initialize();

        setAxisSizeX(35);
        setAxisSizeY(65);
        setTitleHeight(30);
        setBorderSize(5);
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
    protected SimpleTextPainter createTitlePainter() {
        SimpleTextPainter painter = new HistogramBinTextPainter(axis);
        painter.setHorizontalPosition(HorizontalPosition.Left);
        return painter;
    }
}

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

import com.metsci.glimpse.axis.painter.label.GridAxisLabelHandler;
import com.metsci.glimpse.painter.base.GlimpsePainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.plot.SimplePlot2D;
import com.metsci.glimpse.support.font.FontUtils;

import mirur.core.Array1D;
import mirur.plugins.DataUnitConverter.DataAxisUnitConverter;
import mirur.plugins.line1d.MarkerPainter;

public class Array1DPlot extends SimplePlot2D {
    private GlimpsePainter dataPainter;
    private MarkerPainter markerPainter;

    public Array1DPlot(Array1D array) {
        getLabelHandlerY().setTickSpacing(40);

        // since we center on the "array" index
        getAxisX().setMin(-0.5);
        getAxisX().setMax(array.getSize() - 0.5);
        AxisUtils.padAxis(getAxisY());

        setTitlePainterData(array);

        setTitleFont(FontUtils.getDefaultPlain(14));
    }

    public void setPainter(GlimpsePainter dataPainter, int[] indexMap, DataUnitConverter unitConverter, double minData, double maxData) {
        if (this.dataPainter != null) {
            removePainter(this.dataPainter);
        }
        this.dataPainter = dataPainter;

        getLabelHandlerY().setAxisUnitConverter(new DataAxisUnitConverter(unitConverter));
        AxisUtils.adjustAxisToMinMax(minData, maxData, getAxisY(), unitConverter);

        addPainter(dataPainter, DATA_LAYER);

        if (titlePainter instanceof Array1DTitlePainter) {
            ((Array1DTitlePainter) titlePainter).setIndexMap(indexMap);
        }

        if (markerPainter != null) {
            markerPainter.setVisible(indexMap != null);
        }
    }

    public void addMarker(String name, int position) {
        if (markerPainter == null) {
            markerPainter = new MarkerPainter();
            axisLayoutX.addPainter(markerPainter);
        }

        markerPainter.addMarker(name, position);
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

        setAxisSizeX(25);
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
    protected GridAxisLabelHandler createLabelHandlerY() {
        return new HdrAxisLabelHandler();
    }

    @Override
    protected SimpleTextPainter createTitlePainter() {
        SimpleTextPainter painter = new Array1DTitlePainter(getAxisX());
        painter.setHorizontalPosition(HorizontalPosition.Left);
        painter.setVerticalPosition(VerticalPosition.Center);
        return painter;
    }

    protected void setTitlePainterData(Array1D array) {
        ((Array1DTitlePainter) titlePainter).setArray(array);
    }
}

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

import static com.metsci.glimpse.support.color.GlimpseColor.getRed;
import mirur.core.Array1D;
import mirur.core.MinMaxFiniteValueVisitor;
import mirur.core.VisitArray;
import mirur.plugins.line1d.MarkerPainter;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.painter.base.GlimpsePainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.plot.SimplePlot2D;

public class Array1DPlot extends SimplePlot2D {
    private ShaderWrapperPainter shaderWrapper;
    private GlimpsePainter dataPainter;

    public Array1DPlot(GlimpseDataPainter2D dataPainter, Array1D array) {
        setAxisSizeX(25);
        setTitleHeight(30);

        updateAxesBounds(array);
        setTitlePainterData(array);

        shaderWrapper = new ShaderWrapperPainter();
        addPainter(dataPainter, shaderWrapper, DATA_LAYER);
        this.dataPainter = dataPainter;
    }

    public void swapPainter(GlimpseDataPainter2D dataPainter, int[] indexMap) {
        removePainter(this.dataPainter);
        this.dataPainter = dataPainter;

        addPainter(dataPainter, shaderWrapper, DATA_LAYER);
        if (titlePainter instanceof Array1DTitlePainter) {
            ((Array1DTitlePainter) titlePainter).setIndexMap(indexMap);
        }
    }

    public void addMarkerPainter(String name, int position) {
        MarkerPainter p = new MarkerPainter(name, position);
        addPainter(p, FOREGROUND_LAYER - 1);
    }

    @Override
    protected void initializePainters() {
        super.initializePainters();

        getCrosshairPainter().showSelectionBox(false);
        getCrosshairPainter().setCursorColor(getRed());
        titlePainter.setHorizontalPosition(HorizontalPosition.Left);
    }

    @Override
    protected SimpleTextPainter createTitlePainter() {
        SimpleTextPainter painter = new Array1DTitlePainter(getAxis());
        painter.setHorizontalPosition(HorizontalPosition.Left);
        painter.setVerticalPosition(VerticalPosition.Center);
        return painter;
    }

    public void setShaders(InitializablePipeline pipeline) {
        shaderWrapper.setPipeline(pipeline);
    }

    protected void setTitlePainterData(Array1D array) {
        ((Array1DTitlePainter) titlePainter).setArray(array);
    }

    protected void updateAxesBounds(Array1D array) {
        MinMaxFiniteValueVisitor minMaxVisitor = VisitArray.visit(array.getData(), new MinMaxFiniteValueVisitor());
        double min = minMaxVisitor.getMin();
        double max = minMaxVisitor.getMax();

        if (min == max) {
            max++;
        }

        // since we center on the "array" index
        getAxisX().setMin(-0.5);
        getAxisX().setMax(array.getSize() - 0.5);

        getAxisY().setMin(min);
        getAxisY().setMax(max);
        padAxis(getAxisY());
    }

    protected void padAxis(Axis1D axis) {
        double padding = (axis.getMax() - axis.getMin()) * 0.02;
        axis.setMin(axis.getMin() - padding);
        axis.setMax(axis.getMax() + padding);
        axis.validate();
    }
}

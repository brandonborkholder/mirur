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
package mirur.plugins.complex;

import static com.metsci.glimpse.support.color.GlimpseColor.getGray;
import static com.metsci.glimpse.support.color.GlimpseColor.getWhite;
import static com.metsci.glimpse.support.font.FontUtils.getDefaultBold;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.axis.listener.mouse.AxisMouseListener;
import com.metsci.glimpse.axis.painter.NumericAxisPainter;
import com.metsci.glimpse.axis.painter.NumericTopXAxisPainter;
import com.metsci.glimpse.axis.painter.label.GridAxisLabelHandler;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.event.mouse.GlimpseMouseAdapter;
import com.metsci.glimpse.event.mouse.GlimpseMouseEvent;
import com.metsci.glimpse.layout.GlimpseAxisLayout1D;
import com.metsci.glimpse.layout.GlimpseAxisLayout2D;
import com.metsci.glimpse.layout.GlimpseAxisLayoutX;
import com.metsci.glimpse.layout.GlimpseAxisLayoutY;
import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.painter.decoration.BackgroundPainter;
import com.metsci.glimpse.painter.decoration.BorderPainter;
import com.metsci.glimpse.painter.decoration.CrosshairPainter;
import com.metsci.glimpse.painter.decoration.GridPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.plot.Plot2D;
import com.metsci.glimpse.support.font.FontUtils;

import mirur.core.Array1D;
import mirur.core.MinMaxFiniteValueVisitor;
import mirur.core.VisitArray;
import mirur.plugins.AxisUtils;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.DataUnitConverter.DataAxisUnitConverter;
import mirur.plugins.HdrAxisLabelHandler;
import mirur.plugins.ToFloatPrecisionVisitor;
import mirur.plugins.line1d.LinePainter;

public class ComplexPlotLayout extends Plot2D {
    private SimpleTextPainter angleLabelPainter;
    private SimpleTextPainter magnitudeLabelPainter;
    private Complex1DTitlePainter titlePainter1;
    private AngleMagnitude1DTitlePainter titlePainter2;

    private GlimpseAxisLayout1D axisLayoutTopX;
    private NumericAxisPainter painterTopX;

    private GlimpseAxisLayout1D axisLayoutY2;
    private GridAxisLabelHandler tickY2;
    private NumericAxisPainter painterY2;
    private AxisMouseListener mouseListenerY2;

    private Axis2D axisXY2;
    private GlimpseAxisLayout2D axisLayoutXY2;
    private AxisMouseListener mouseListenerXY2;

    private boolean anglePlotOpen;
    private boolean magnitudePlotOpen;

    public ComplexPlotLayout() {
        initialize();

        setAxisSizeX(25);
        setAxisSizeY(65);
        setTitleHeight(45);
        setBorderSize(5);

        toggleOpenPlot(true, true);
        toggleOpenPlot(false, true);
        titlePainter1.setFont(FontUtils.getDefaultPlain(14));
        titlePainter2.setFont(FontUtils.getDefaultPlain(14));
    }

    @Override
    protected void initializeAxes() {
        super.initializeAxes();
        axisXY2 = new Axis2D(getAxisX(), createAxisY());
    }

    @Override
    protected void initializePainters() {
        super.initializePainters();

        removeLayout(axisLayoutZ);

        titlePainter1 = new Complex1DTitlePainter(getAxisX());
        titlePainter1.setHorizontalPosition(HorizontalPosition.Left);
        titlePainter1.setVerticalPosition(VerticalPosition.Top);
        titlePainter1.setVerticalPadding(0);
        titlePainter2 = new AngleMagnitude1DTitlePainter(getAxisX());
        titlePainter2.setHorizontalPosition(HorizontalPosition.Left);
        titlePainter2.setVerticalPosition(VerticalPosition.Bottom);
        titlePainter2.setVerticalPadding(5);
        titleLayout.addPainter(titlePainter1);
        titleLayout.addPainter(titlePainter2);

        axisLayoutTopX = new GlimpseAxisLayoutX(this, "AxisXTop");
        axisLayoutY2 = new GlimpseAxisLayoutY(this, "AxisY2");
        axisLayoutXY2 = new GlimpseAxisLayout2D(this, "Center2");

        axisLayoutTopX.setAxis(getAxisX());
        axisLayoutY2.setAxis(getAxisY2());
        axisLayoutXY2.setAxis(axisXY2);

        tickY2 = createLabelHandlerY();

        painterY2 = createAxisPainterY(tickY2);
        axisLayoutY2.addPainter(painterY2);

        painterTopX = createAxisPainterTopX();
        axisLayoutTopX.addPainter(painterTopX);

        angleLabelPainter = createPlotLabelPainter(true);
        axisLayoutTopX.addPainter(angleLabelPainter, Plot2D.FOREGROUND_LAYER);
        magnitudeLabelPainter = createPlotLabelPainter(false);
        axisLayoutX.addPainter(magnitudeLabelPainter, Plot2D.FOREGROUND_LAYER);

        mouseListenerY2 = createAxisMouseListenerY();
        mouseListenerXY2 = createAxisMouseListenerXY();

        for (GlimpseLayout layout : new GlimpseLayout[] { getLayoutXY1(), getLayoutXY2() }) {
            BackgroundPainter plotBackgroundPainter = new BackgroundPainter(false);
            layout.addPainter(plotBackgroundPainter, Integer.MIN_VALUE);

            GridPainter gridPainter = new GridPainter(tickX, tickY);
            layout.addPainter(gridPainter, Plot2D.BACKGROUND_LAYER);

            BorderPainter borderPainter = new BorderPainter();
            layout.addPainter(borderPainter, Plot2D.FOREGROUND_LAYER);

            CrosshairPainter crosshairPainter = new CrosshairPainter();
            crosshairPainter.showSelectionBox(false);
            layout.addPainter(crosshairPainter, Plot2D.FOREGROUND_LAYER);
        }

        titlePainter.setHorizontalPosition(HorizontalPosition.Left);
    }

    protected SimpleTextPainter createPlotLabelPainter(boolean isAngle) {
        SimpleTextPainter labelPainter = new SimpleTextPainter();
        labelPainter.setBackgroundColor(getGray());
        labelPainter.setColor(getWhite());
        labelPainter.setFont(getDefaultBold(14));
        labelPainter.setHorizontalPosition(HorizontalPosition.Right);
        labelPainter.setVerticalPosition(isAngle ? VerticalPosition.Bottom : VerticalPosition.Top);
        labelPainter.setPaintBackground(true);
        labelPainter.setPaintBorder(false);
        labelPainter.setText(isAngle ? "Angle" : "Magnitude");
        labelPainter.setVerticalPadding(isAngle ? 3 : 0);
        return labelPainter;
    }

    private void toggleOpenPlot(boolean angle, boolean open) {
        if (angle) {
            anglePlotOpen = open;
            angleLabelPainter.setText("Angle " + (open ? "\u2014" : "+"));
        } else {
            magnitudePlotOpen = open;
            magnitudeLabelPainter.setText("Magnitude " + (open ? "\u2014" : "+"));
        }

        updatePainterLayout();
    }

    @Override
    protected void attachAxisMouseListeners() {
        super.attachAxisMouseListeners();

        attachAxisMouseListener(axisLayoutY2, mouseListenerY2);
        attachAxisMouseListener(axisLayoutXY2, mouseListenerXY2);
        attachAxisMouseListener(axisLayoutTopX, mouseListenerX);

        axisLayoutTopX.addGlimpseMouseListener(new GlimpseMouseAdapter() {
            @Override
            public void mouseReleased(GlimpseMouseEvent event) {
                GlimpseBounds bounds = event.getTargetStack().getBounds();
                if (bounds.getWidth() - 20 < event.getX() && bounds.getHeight() - 20 < event.getY()) {
                    toggleOpenPlot(true, !anglePlotOpen);
                }
            }
        });
        axisLayoutX.addGlimpseMouseListener(new GlimpseMouseAdapter() {
            @Override
            public void mouseReleased(GlimpseMouseEvent event) {
                GlimpseBounds bounds = event.getTargetStack().getBounds();
                if (bounds.getWidth() - 20 < event.getX() && event.getY() < 20) {
                    toggleOpenPlot(false, !magnitudePlotOpen);
                }
            }
        });
    }

    @Override
    protected void updatePainterLayout() {
        super.updatePainterLayout();

        getLayoutManager().setLayoutConstraints(
                String.format("bottomtotop, gapx 0, gapy 0, insets %d %d %d %d", 0, outerBorder, outerBorder, outerBorder));

        titleLayout.setLayoutData(String.format("cell 0 0 2 1, growx, height %d!", titleSpacing));
        axisLayoutTopX.setLayoutData(String.format("cell 1 1 1 1, pushx, growx, height %d!", axisThicknessX));
        axisLayoutY.setLayoutData(String.format("cell 0 2 1 1, pushy, growy, width %d!", axisThicknessY));
        axisLayoutXY.setLayoutData("cell 1 2 1 1, push, grow");
        axisLayoutY2.setLayoutData(String.format("cell 0 3 1 1, pushy, growy, width %d!", axisThicknessY));
        axisLayoutXY2.setLayoutData("cell 1 3 1 1, push, grow");
        axisLayoutX.setLayoutData(String.format("cell 1 4 1 1, pushx, growx, height %d!", axisThicknessX));

        if (!anglePlotOpen) {
            axisLayoutY.setLayoutData("width 0!, height 0!");
            axisLayoutXY.setLayoutData("width 0!, height 0!");
        }

        if (!magnitudePlotOpen) {
            axisLayoutY2.setLayoutData("width 0!, height 0!");
            axisLayoutXY2.setLayoutData("width 0!, height 0!");
        }

        invalidateLayout();
    }

    protected NumericAxisPainter createAxisPainterTopX() {
        NumericTopXAxisPainter painter = new NumericTopXAxisPainter(tickX);
        painter.setTickLabelBufferSize(4);
        painter.setTickBufferSize(0);
        return painter;
    }

    @Override
    protected GridAxisLabelHandler createLabelHandlerY() {
        GridAxisLabelHandler handler = new HdrAxisLabelHandler();
        handler.setTickSpacing(40);
        return handler;
    }

    public void setComplexData(Array1D complex, Array1D magnitudes, Array1D angles) {
        MinMaxFiniteValueVisitor minMax = VisitArray.visit(angles.getData(), new MinMaxFiniteValueVisitor());
        DataUnitConverter unitConverter = ToFloatPrecisionVisitor.create(minMax.getMin(), minMax.getMax());
        getLayoutXY1().addPainter(new LinePainter(angles, unitConverter));
        updateAxesBounds(angles, axisXY, minMax.getMin(), minMax.getMax(), unitConverter);
        getLabelHandlerY1().setAxisUnitConverter(new DataAxisUnitConverter(unitConverter));

        minMax = VisitArray.visit(magnitudes.getData(), new MinMaxFiniteValueVisitor());
        unitConverter = ToFloatPrecisionVisitor.create(minMax.getMin(), minMax.getMax());
        getLayoutXY2().addPainter(new LinePainter(magnitudes, unitConverter));
        updateAxesBounds(magnitudes, axisXY2, minMax.getMin(), minMax.getMax(), unitConverter);
        getLabelHandlerY2().setAxisUnitConverter(new DataAxisUnitConverter(unitConverter));

        titlePainter1.setArray(complex);
        titlePainter2.setArrays(angles, magnitudes);
    }

    protected void updateAxesBounds(Array1D array, Axis2D axis, double dataMin, double dataMax, DataUnitConverter unitConverter) {
        axis.getAxisX().setMin(-0.5);
        axis.getAxisX().setMax(array.getSize() - 0.5);

        AxisUtils.adjustAxisToMinMax(dataMin, dataMax, axis.getAxisY(), unitConverter);
        AxisUtils.padAxis(axis.getAxisY());
        axis.validate();
    }

    protected GlimpseAxisLayout2D getLayoutXY1() {
        return axisLayoutXY;
    }

    protected GlimpseAxisLayout2D getLayoutXY2() {
        return axisLayoutXY2;
    }

    protected GridAxisLabelHandler getLabelHandlerY1() {
        return tickY;
    }

    protected GridAxisLabelHandler getLabelHandlerY2() {
        return tickY2;
    }

    public Axis1D getAxisY1() {
        return getAxisY();
    }

    public Axis1D getAxisY2() {
        return axisXY2.getAxisY();
    }
}

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
package mirur.plugins.shape;

import static com.metsci.glimpse.core.plot.Plot2D.BACKGROUND_LAYER;
import static com.metsci.glimpse.core.plot.Plot2D.DATA_LAYER;
import static mirur.plugins.AxisUtils.padAxis2d;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.core.axis.painter.label.AxisUnitConverter;
import com.metsci.glimpse.core.canvas.GlimpseCanvas;
import com.metsci.glimpse.core.painter.decoration.GridPainter;
import com.metsci.glimpse.core.painter.shape.LineSetPainter;
import com.metsci.glimpse.core.plot.SimplePlot2D;
import com.metsci.glimpse.core.support.shader.line.LineJoinType;
import com.metsci.glimpse.core.support.shader.line.LineStyle;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import mirur.core.VariableObject;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.MirurView;

public class ShapeView implements MirurView {
    @Override
    public boolean supportsData(VariableObject obj) {
        return obj.getData() instanceof Shape;
    }

    @Override
    public String getName() {
        return "Shape";
    }

    @Override
    public ImageDescriptor getIcon() {
        return null;
    }

    @Override
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        Shape shape = (Shape) obj.getData();

        List<float[]> xs = new ArrayList<>();
        List<float[]> ys = new ArrayList<>();

        final Rectangle2D bounds = shape.getBounds2D();

        AffineTransform xform = new AffineTransform();
        xform.concatenate(AffineTransform.getTranslateInstance(0, bounds.getMaxY()));
        xform.concatenate(AffineTransform.getScaleInstance(1, -1));

        PathIterator itr = shape.getPathIterator(xform, 30);
        float[] coords = new float[2];
        FloatList x = new FloatArrayList();
        FloatList y = new FloatArrayList();
        while (!itr.isDone()) {
            int type = itr.currentSegment(coords);
            switch (type) {
            case PathIterator.SEG_MOVETO:
                if (x.size() > 0) {
                    xs.add(x.toFloatArray());
                    ys.add(y.toFloatArray());
                    x = new FloatArrayList();
                    y = new FloatArrayList();
                }

            case PathIterator.SEG_LINETO:
                x.add(coords[0]);
                y.add(coords[1]);
                break;

            case PathIterator.SEG_CLOSE:
                x.add(x.getFloat(0));
                y.add(y.getFloat(0));
                break;

            default:
                throw new AssertionError();
            }

            itr.next();
        }

        xs.add(x.toFloatArray());
        ys.add(y.toFloatArray());

        LineSetPainter painter = new LineSetPainter();
        painter.setData(xs.toArray(new float[0][]), ys.toArray(new float[0][]));
        LineStyle style = new LineStyle();
        style.thickness_PX = 2;
        style.joinType = LineJoinType.JOIN_MITER;
        painter.setLineStyle(style);

        SimplePlot2D plot = new SimplePlot2D();
        plot.getGridPainter().setShowMinorGrid(false);
        plot.getCrosshairPainter().showSelectionBox(false);
        plot.getLayoutCenter().addPainter(new GridPainter(), BACKGROUND_LAYER);
        plot.getLayoutCenter().addPainter(painter, DATA_LAYER);
        plot.getLabelHandlerY().setTickSpacing(40);
        plot.getLabelHandlerY().setAxisUnitConverter(new AxisUnitConverter() {
            @Override
            public double toAxisUnits(double value) {
                return bounds.getHeight() - value;
            }

            @Override
            public double fromAxisUnits(double value) {
                return bounds.getHeight() - value;
            }
        });
        plot.setTitleHeight(0);

        plot.getAxisX().setMin(bounds.getMinX());
        plot.getAxisX().setMax(bounds.getMaxX());
        plot.getAxisY().setMin(0);
        plot.getAxisY().setMax(bounds.getHeight());
        padAxis2d(plot.getAxis());
        plot.getAxis().validate();

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());
        return result;
    }
}

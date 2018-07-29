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

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.base.GlimpsePainter;

import mirur.core.Array1D;
import mirur.core.Array1DImpl;
import mirur.core.MinMaxFiniteValueVisitor;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.heatmap2d.ScaleChooserAction;
import mirur.plugins.heatmap2d.ScaleOperator;

public abstract class SimplePlugin1D implements MirurView {
    private final String name;
    private final ImageDescriptor icon;

    public SimplePlugin1D(String name, ImageDescriptor icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public boolean supportsData(VariableObject var) {
        Object obj = var.getData();
        if (var instanceof Array1D) {
            Class<?> clazz = obj.getClass();
            return int[].class.equals(clazz) ||
                    long[].class.equals(clazz) ||
                    float[].class.equals(clazz) ||
                    double[].class.equals(clazz) ||
                    char[].class.equals(clazz) ||
                    byte[].class.equals(clazz) ||
                    short[].class.equals(clazz) ||
                    boolean[].class.equals(clazz);
        } else if (obj instanceof Buffer) {
            return ((Buffer) obj).hasArray() &&
                    (obj instanceof ByteBuffer ||
                            obj instanceof ShortBuffer ||
                            obj instanceof CharBuffer ||
                            obj instanceof IntBuffer ||
                            obj instanceof FloatBuffer ||
                            obj instanceof DoubleBuffer ||
                            obj instanceof LongBuffer);
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageDescriptor getIcon() {
        return icon;
    }

    @Override
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        Array1D array1d;
        if (obj instanceof Array1D) {
            array1d = (Array1D) obj;
        } else {
            Buffer buf = (Buffer) obj.getData();
            Object arrayObj = buf.array();
            if (buf.arrayOffset() != 0) {
                Object newArrayObj = Array.newInstance(arrayObj.getClass().getComponentType(), buf.capacity());
                System.arraycopy(arrayObj, buf.arrayOffset(), newArrayObj, 0, buf.capacity());
                arrayObj = newArrayObj;
            }
            array1d = new Array1DImpl(obj.getName(), arrayObj);
        }

        MinMaxFiniteValueVisitor minMaxVisitor = new MinMaxFiniteValueVisitor();
        VisitArray.visit(array1d.getData(), minMaxVisitor);
        DataUnitConverter unitConverter = ToFloatPrecisionVisitor.create(minMaxVisitor.getMin(), minMaxVisitor.getMax());

        Array1DPlot plot = new Array1DPlot(array1d, minMaxVisitor.getMin(), minMaxVisitor.getMax());
        GlimpsePainter painter = createPainter(array1d, unitConverter);
        plot.setPainter(painter, unitConverter);

        if (obj.getData() instanceof Buffer) {
            Buffer buf = (Buffer) obj.getData();
            plot.addMarker("position", buf.position());
            plot.addMarker("limit", buf.limit());
        }

        DataPainterImpl result = new DataPainterImpl(plot);

        result.addAction(new SortAction(array1d) {
            @Override
            protected void swapPainter(Array1D array1d, int[] indexMap) {
                GlimpsePainter newPainter = createPainter(array1d, plot.getUnitConverter());
                plot.setPainter(newPainter, plot.getUnitConverter());
                plot.setOrder(indexMap);
            }
        });
        result.addAction(new ScaleChooserAction("Y Scale") {
            @Override
            protected void select(ScaleOperator old, ScaleOperator op, boolean updateAxes) {
                DataUnitConverter unitConverter = new DataUnitConverter.ScaleOpConverter(op);
                GlimpsePainter newPainter = createPainter(array1d, unitConverter);
                plot.setPainter(newPainter, unitConverter);
            }
        });

        result.addAxis(plot.getAxis());
        return result;
    }

    protected abstract GlimpsePainter createPainter(Array1D array, DataUnitConverter unitConverter);
}

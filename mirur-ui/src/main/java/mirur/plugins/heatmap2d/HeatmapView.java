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
package mirur.plugins.heatmap2d;

import static java.lang.Double.isFinite;

import java.nio.FloatBuffer;

import com.metsci.glimpse.axis.tagged.Tag;
import com.metsci.glimpse.axis.tagged.TaggedAxis1D;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.gl.texture.ColorTexture1D;
import com.metsci.glimpse.support.colormap.ColorGradient;
import com.metsci.glimpse.support.colormap.ColorGradients;
import com.metsci.glimpse.support.projection.FlatProjection;
import com.metsci.glimpse.support.projection.Projection;
import com.metsci.glimpse.support.texture.FloatTextureProjected2D;
import com.metsci.glimpse.support.texture.FloatTextureProjected2D.MutatorFloat2D;

import mirur.core.Array2D;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.AxisUtils;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.SimplePlugin2D;

public class HeatmapView extends SimplePlugin2D {
    public HeatmapView() {
        super("Heatmap", null);
    }

    @Override
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        final Array2D array = (Array2D) obj;

        HeatmapArrayPlot plot = new HeatmapArrayPlot(array);

        final ColorTexture1D colors = new ColorTexture1D(1024);
        colors.setColorGradient(ColorGradients.jet);

        int dim0 = array.getMaxSize(0);
        int dim1 = array.getMaxSize(1);

        final FloatTextureProjected2D texture = new FloatTextureProjected2D(dim0, dim1);

        Projection projection = new FlatProjection(0, dim0, 0, dim1);
        texture.setProjection(projection);
        texture.mutate(new MutatorFloat2D() {
            @Override
            public void mutate(FloatBuffer data, int dataSizeX, int dataSizeY) {
                data.clear();
                if (array.isJagged()) {
                    VisitArray.visit2d(array.getData(), new JaggedToFloatBufferVisitor(data, dataSizeY, Float.NaN));
                } else {
                    VisitArray.visit2d(array.getData(), new ToFloatBufferVisitor(data));
                }
            }
        });

        plot.setData(texture);
        plot.setColorScale(colors);

        final Tag t1 = plot.getTag1();
        final Tag t2 = plot.getTag2();
        final TaggedAxis1D axisZ = plot.getAxisZ();

        plot.getAxisX().setMin(0);
        plot.getAxisX().setMax(dim0);
        plot.getAxisY().setMin(0);
        plot.getAxisY().setMax(dim1);
        plot.getAxis().validate();

        DataUnitConverter unitConverter = DataUnitConverter.IDENTITY;
        AxisUtils.adjustAxisToMinMax(array, plot.getAxisZ(), unitConverter);
        t1.setValue(plot.getAxisZ().getMin());
        t2.setValue(plot.getAxisZ().getMax());
        AxisUtils.padAxis(plot.getAxisZ());

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());
        result.addAxis(plot.getAxisZ());
        result.addAction(new GradientChooserAction() {
            @Override
            protected void select(ColorGradient gradient) {
                colors.setColorGradient(gradient);
            }
        });
        result.addAction(new ScaleChooserAction() {
            @Override
            protected void select(final ScaleOperator old, final ScaleOperator op, final boolean updateAxes) {
                texture.mutate(new MutatorFloat2D() {
                    @Override
                    public void mutate(FloatBuffer data, int dataSizeX, int dataSizeY) {
                        data.clear();
                        if (array.isJagged()) {
                            VisitArray.visit2d(array.getData(), new JaggedToFloatBufferVisitor(data, dataSizeY, Float.NaN));
                        } else {
                            VisitArray.visit2d(array.getData(), new ToFloatBufferVisitor(data));
                        }

                        data.flip();
                        op.operate(data);

                        if (updateAxes) {
                            double min = axisZ.getMin();
                            min = op.operate(old.unoperate(min));
                            double max = axisZ.getMax();
                            max = op.operate(old.unoperate(max));

                            if (!isFinite(min) && !isFinite(max)) {
                                min = -1;
                                max = 1;
                            } else if (!isFinite(max)) {
                                max = min + 1;
                            } else if (!isFinite(min)) {
                                min = max - 1;
                            }
                            axisZ.setMin(min);
                            axisZ.setMax(max);

                            double v = t1.getValue();
                            v = op.operate(old.unoperate(v));
                            v = isFinite(v) ? v : min;
                            t1.setValue(v);
                            v = t2.getValue();
                            v = op.operate(old.unoperate(v));
                            v = isFinite(v) ? v : max;
                            t2.setValue(v);
                        }
                    }
                });

                axisZ.validateTags();
                axisZ.validate();
            }
        });
        return result;
    }
}

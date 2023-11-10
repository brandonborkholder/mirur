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
package mirur.plugins.heatmap2d;

import static java.lang.Double.isFinite;

import java.nio.FloatBuffer;

import com.metsci.glimpse.core.axis.tagged.Tag;
import com.metsci.glimpse.core.axis.tagged.TaggedAxis1D;
import com.metsci.glimpse.core.canvas.GlimpseCanvas;
import com.metsci.glimpse.core.support.colormap.ColorGradient;
import com.metsci.glimpse.core.support.texture.FloatTextureProjected2D.MutatorFloat2D;

import mirur.core.Array2D;
import mirur.core.MinMaxFiniteValueVisitor;
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

        final HeatmapArrayPlot plot = new HeatmapArrayPlot(array);

        plot.getTexture().mutate(new MutatorFloat2D() {
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

        final Tag t1 = plot.getTag1();
        final Tag t2 = plot.getTag2();
        final TaggedAxis1D axisZ = plot.getAxisZ();

        DataUnitConverter unitConverter = DataUnitConverter.IDENTITY;
        final MinMaxFiniteValueVisitor minMaxVisitor = new MinMaxFiniteValueVisitor();
        VisitArray.visit(array.getData(), minMaxVisitor);
        AxisUtils.adjustAxisToMinMax(minMaxVisitor.getMin(), minMaxVisitor.getMax(), plot.getAxisZ(), unitConverter);
        t1.setValue(plot.getAxisZ().getMin());
        t2.setValue(plot.getAxisZ().getMax());
        AxisUtils.padAxis(plot.getAxisZ());

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());
        result.addAxis(plot.getAxisZ());
        result.addAction(new ToggleTransposeAction() {
            @Override
            public void run() {
                plot.setTranpose(isChecked());
            }
        });
        result.addAction(new GradientChooserAction() {
            @Override
            protected void select(ColorGradient gradient) {
                plot.setColorGradient(gradient);
            }
        });
        result.addAction(new ScaleChooserAction("Color Axis Scale") {
            @Override
            protected void select(final ScaleOperator op) {
                plot.getTexture().mutate(new MutatorFloat2D() {
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

                        double min = minMaxVisitor.getMin();
                        min = op.operate(min);
                        double max = minMaxVisitor.getMax();
                        max = op.operate(max);

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
                        t1.setValue(min);
                        t2.setValue(max);
                    }
                });

                axisZ.validateTags();
                axisZ.validate();
            }
        });
        return result;
    }
}

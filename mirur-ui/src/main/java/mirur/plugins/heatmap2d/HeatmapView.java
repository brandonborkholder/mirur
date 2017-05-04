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

import java.nio.FloatBuffer;
import java.util.Map;

import com.metsci.glimpse.axis.tagged.NamedConstraint;
import com.metsci.glimpse.axis.tagged.Tag;
import com.metsci.glimpse.axis.tagged.TaggedAxis1D;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.gl.texture.ColorTexture1D;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.painter.texture.TaggedHeatMapPainter;
import com.metsci.glimpse.plot.Plot2D;
import com.metsci.glimpse.plot.TaggedColorAxisPlot2D;
import com.metsci.glimpse.support.color.GlimpseColor;
import com.metsci.glimpse.support.colormap.ColorGradient;
import com.metsci.glimpse.support.colormap.ColorGradients;
import com.metsci.glimpse.support.font.FontUtils;
import com.metsci.glimpse.support.projection.FlatProjection;
import com.metsci.glimpse.support.projection.Projection;
import com.metsci.glimpse.support.texture.FloatTextureProjected2D;
import com.metsci.glimpse.support.texture.FloatTextureProjected2D.MutatorFloat2D;

import mirur.core.Array2D;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.Array2DTitlePainter;
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

		TaggedColorAxisPlot2D plot = new TaggedColorAxisPlot2D() {
			@Override
			protected void initialize() {
				super.initialize();

				setTitleHeight(30);
				setBorderSize(5);
				getAxisPainterX().setAxisLabelBufferSize(3);
				getLabelHandlerY().setTickSpacing(50);
				getLabelHandlerZ().setTickSpacing(50);
				setTitleFont(FontUtils.getDefaultPlain(14));
			}

			@Override
			protected SimpleTextPainter createTitlePainter() {
				SimpleTextPainter painter = new Array2DTitlePainter(getAxis());
				painter.setHorizontalPosition(HorizontalPosition.Left);
				painter.setVerticalPosition(VerticalPosition.Center);
				painter.setColor(GlimpseColor.getBlack());
				return painter;
			}

			@Override
			protected void updatePainterLayout() {
				super.updatePainterLayout();

				getLayoutManager().setLayoutConstraints(String.format("bottomtotop, gapx 0, gapy 0, insets %d %d %d %d",
						0, outerBorder, outerBorder, outerBorder));
				titleLayout.setLayoutData(String.format("cell 0 0 2 1, growx, height %d!", titleSpacing));
				axisLayoutZ.setLayoutData(String.format("cell 2 0 1 3, growy, width %d!", axisThicknessZ));

				invalidateLayout();
			}

			@Override
			protected void initializePainters() {
				super.initializePainters();

				((Array2DTitlePainter) titlePainter).setArray(array);
			}
		};

		plot.setAxisLabelX(array.getName() + "[]");
		plot.setAxisLabelY(array.getName() + "[][]");

		plot.getCrosshairPainter().showSelectionBox(false);

		final TaggedAxis1D axisZ = plot.getAxisZ();
		TaggedHeatMapPainter painter = new TaggedHeatMapPainter(axisZ);

		final Tag t1 = axisZ.addTag("T1", 0.0).setAttribute(Tag.TEX_COORD_ATTR, 0.0f);
		final Tag t2 = axisZ.addTag("T2", 0.0).setAttribute(Tag.TEX_COORD_ATTR, 1.0f);

		axisZ.addConstraint(new NamedConstraint("C1") {
			@Override
			public void applyConstraint(TaggedAxis1D axis, Map<String, Tag> previousTags) {
				if (t1.getValue() > t2.getValue()) {
					t1.setValue(t2.getValue());
				}
			}
		});

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

		painter.setData(texture);
		painter.setColorScale(colors);

		plot.addPainter(painter, Plot2D.DATA_LAYER);
		plot.setColorScale(painter.getColorScale());

		plot.getAxisX().setMin(0);
		plot.getAxisX().setMax(dim0);
		plot.getAxisY().setMin(0);
		plot.getAxisY().setMax(dim1);
		plot.getAxis().validate();

		DataUnitConverter unitConverter = DataUnitConverter.IDENTITY;
		AxisUtils.adjustAxisToMinMax(array, plot.getAxisZ(), unitConverter);
		t1.setValue(axisZ.getMin());
		t2.setValue(axisZ.getMax());
		AxisUtils.padAxis(axisZ);

		DataPainterImpl result = new DataPainterImpl(plot);
		result.addAxis(plot.getAxis());
		result.addAxis(plot.getAxisZ());
		result.addAction(new GradientChooserAction() {
			@Override
			protected void select(ColorGradient gradient) {
				colors.setColorGradient(gradient);
			}
		});
		result.addAction(new LogOption() {
			@Override
			protected void select(final ScaleOperator old, final ScaleOperator op) {
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

						double v = axisZ.getMin();
						v = op.operate(old.unoperate(v));
						axisZ.setMin(v);
						v = axisZ.getMax();
						v = op.operate(old.unoperate(v));
						axisZ.setMax(v);
						v = t1.getValue();
						v = op.operate(old.unoperate(v));
						t1.setValue(v);
						v = t2.getValue();
						v = op.operate(old.unoperate(v));
						t2.setValue(v);

						axisZ.validateTags();
						axisZ.validate();
					}
				});
			}
		});
		return result;
	}
}

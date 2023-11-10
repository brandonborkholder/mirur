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

import java.util.Map;

import com.metsci.glimpse.core.axis.tagged.NamedConstraint;
import com.metsci.glimpse.core.axis.tagged.Tag;
import com.metsci.glimpse.core.axis.tagged.TaggedAxis1D;
import com.metsci.glimpse.core.gl.texture.ColorTexture1D;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.core.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.core.painter.texture.TaggedHeatMapPainter;
import com.metsci.glimpse.core.plot.TaggedColorAxisPlot2D;
import com.metsci.glimpse.core.support.color.GlimpseColor;
import com.metsci.glimpse.core.support.colormap.ColorGradient;
import com.metsci.glimpse.core.support.colormap.ColorGradients;
import com.metsci.glimpse.core.support.font.FontUtils;
import com.metsci.glimpse.core.support.projection.FlatProjection;
import com.metsci.glimpse.core.support.texture.FloatTextureProjected2D;

import mirur.core.Array2D;
import mirur.plugins.Array2DTitlePainter;

public final class HeatmapArrayPlot extends TaggedColorAxisPlot2D {
    private final Array2D array;
    private TaggedHeatMapPainter heatmapPainter;
    private FloatTextureProjected2D texture;

    private final int dim1;
    private final int dim2;

    public HeatmapArrayPlot(Array2D array) {
        this.array = array;

        dim1 = array.getMaxSize(0);
        dim2 = array.getMaxSize(1);

        texture = new FloatTextureProjected2D(dim1, dim2);
        heatmapPainter.setData(texture);

        ((Array2DTitlePainter) titlePainter).setArray(array);

        ColorTexture1D colors = new ColorTexture1D(1024);
        setColorScale(colors);
        setColorGradient(ColorGradients.jet);

        setTranpose(false);
    }

    public void setTranpose(boolean transpose) {
        if (transpose) {
            FlatProjection projection = new FlatProjection(0, dim1, 0, dim2);
            texture.setProjection(projection);
            ((Array2DTitlePainter) titlePainter).setProjection(projection);

            setAxisLabelX(array.getName() + "[]");
            setAxisLabelY(array.getName() + "[][]");
            getAxisX().setMin(0);
            getAxisX().setMax(dim1);
            getAxisY().setMin(0);
            getAxisY().setMax(dim2);
            getAxis().validate();
        } else {
            FlippedFlatProjection projection = new FlippedFlatProjection(0, dim2, 0, dim1);
            texture.setProjection(projection);
            ((Array2DTitlePainter) titlePainter).setProjection(projection);

            setAxisLabelX(array.getName() + "[][]");
            setAxisLabelY(array.getName() + "[]");
            getAxisX().setMin(0);
            getAxisX().setMax(dim2);
            getAxisY().setMin(0);
            getAxisY().setMax(dim1);
            getAxis().validate();
        }
    }

    public void setColorGradient(ColorGradient colorGrad) {
        heatmapPainter.getColorScale().setColorGradient(colorGrad);
    }

    @Override
    public void setColorScale(ColorTexture1D colorScale) {
        super.setColorScale(colorScale);
        heatmapPainter.setColorScale(colorScale);
    }

    @Override
    protected void initialize() {
        super.initialize();

        setTitleHeight(30);
        setBorderSize(5);
        getAxisPainterX().setAxisLabelBufferSize(3);
        getLabelHandlerY().setTickSpacing(50);
        getLabelHandlerZ().setTickSpacing(50);
        setTitleFont(FontUtils.getDefaultPlain(14));
        getCrosshairPainter().showSelectionBox(false);
    }

    @Override
    protected void initializeAxes() {
        super.initializeAxes();

        TaggedAxis1D axisZ = getAxisZ();
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

        heatmapPainter = new TaggedHeatMapPainter(getAxisZ());
        addPainter(heatmapPainter, DATA_LAYER);
    }

    public Tag getTag1() {
        return getAxisZ().getTag("T1");
    }

    public Tag getTag2() {
        return getAxisZ().getTag("T2");
    }

    public FloatTextureProjected2D getTexture() {
        return texture;
    }
}

package mirur.plugins.heatmap2d;

import java.util.Map;

import com.metsci.glimpse.axis.tagged.NamedConstraint;
import com.metsci.glimpse.axis.tagged.Tag;
import com.metsci.glimpse.axis.tagged.TaggedAxis1D;
import com.metsci.glimpse.gl.texture.ColorTexture1D;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.painter.texture.TaggedHeatMapPainter;
import com.metsci.glimpse.plot.TaggedColorAxisPlot2D;
import com.metsci.glimpse.support.color.GlimpseColor;
import com.metsci.glimpse.support.font.FontUtils;
import com.metsci.glimpse.support.texture.FloatTextureProjected2D;

import mirur.core.Array2D;
import mirur.plugins.Array2DTitlePainter;

public final class HeatmapArrayPlot extends TaggedColorAxisPlot2D {
    private final Array2D array;
    private TaggedHeatMapPainter heatmapPainter;

    public HeatmapArrayPlot(Array2D array) {
        this.array = array;

        setAxisLabelX(array.getName() + "[]");
        setAxisLabelY(array.getName() + "[][]");

        initialize();
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

        ((Array2DTitlePainter) titlePainter).setArray(array);
    }

    public Tag getTag1() {
        return getAxisZ().getTag("T1");
    }

    public Tag getTag2() {
        return getAxisZ().getTag("T2");
    }

    public void setData(FloatTextureProjected2D texture) {
        heatmapPainter.setData(texture);
    }
}
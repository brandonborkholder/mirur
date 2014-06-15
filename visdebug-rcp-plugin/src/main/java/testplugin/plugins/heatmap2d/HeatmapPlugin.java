package testplugin.plugins.heatmap2d;

import testplugin.plugins.SimplePlugin2D;
import testplugin.views.Array2D;

import com.metsci.glimpse.axis.tagged.NamedConstraint;
import com.metsci.glimpse.axis.tagged.Tag;
import com.metsci.glimpse.axis.tagged.TaggedAxis1D;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.gl.texture.ColorTexture1D;
import com.metsci.glimpse.painter.texture.TaggedHeatMapPainter;
import com.metsci.glimpse.plot.Plot2D;
import com.metsci.glimpse.plot.TaggedColorAxisPlot2D;
import com.metsci.glimpse.support.colormap.ColorGradients;
import com.metsci.glimpse.support.projection.FlatProjection;
import com.metsci.glimpse.support.projection.Projection;
import com.metsci.glimpse.support.texture.FloatTextureProjected2D;

public class HeatmapPlugin extends SimplePlugin2D {
    public HeatmapPlugin() {
        super("Heatmap", null);
    }

    @Override
    protected void installLayout(GlimpseCanvas canvas, Array2D array) {
        TaggedColorAxisPlot2D plot = new TaggedColorAxisPlot2D();

        plot.setTitle(array.getName());
        plot.setAxisLabelX(array.getName() + "[]");
        plot.setAxisLabelY(array.getName() + "[][]");

        plot.getCrosshairPainter().showSelectionBox(false);

        TaggedAxis1D axisZ = plot.getAxisZ();
        TaggedHeatMapPainter painter = new TaggedHeatMapPainter(axisZ);

        final Tag t1 = axisZ.addTag("T1", 0.0).setAttribute(Tag.TEX_COORD_ATTR, 0.0f);
        final Tag t2 = axisZ.addTag("T2", 0.0).setAttribute(Tag.TEX_COORD_ATTR, 1.0f);

        axisZ.addConstraint(new NamedConstraint("C1") {
            @Override
            public void applyConstraint(TaggedAxis1D axis) {
                if (t1.getValue() > t2.getValue()) {
                    t1.setValue(t2.getValue());
                }
            }
        });

        ColorTexture1D colors = new ColorTexture1D(1024);
        colors.setColorGradient(ColorGradients.gray);

        int dim0 = array.getSize(0);
        int dim1 = array.getSize(1);

        FloatTextureProjected2D texture = new FloatTextureProjected2D(dim0, dim1);

        Projection projection = new FlatProjection(0, dim0, 0, dim1);
        texture.setProjection(projection);
        texture.setData(array.toFloats(), true);

        painter.setData(texture);
        painter.setColorScale(colors);

        plot.addPainter(painter, Plot2D.DATA_LAYER);
        plot.setColorScale(painter.getColorScale());

        plot.getAxisX().setMin(0);
        plot.getAxisX().setMax(dim0);
        plot.getAxisY().setMin(0);
        plot.getAxisY().setMax(dim1);

        float minZ = Float.POSITIVE_INFINITY;
        float maxZ = Float.NEGATIVE_INFINITY;
        float[][] data = array.toFloats();
        for (float[] row : data) {
            for (float v : row) {
                minZ = Math.min(minZ, v);
                maxZ = Math.max(maxZ, v);
            }
        }
        plot.getAxisZ().setMin(minZ);
        plot.getAxisZ().setMax(maxZ);

        t1.setValue(minZ);
        t2.setValue(maxZ);

        canvas.addLayout(plot);
    }
}

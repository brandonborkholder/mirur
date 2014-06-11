package testplugin.views;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.gl.texture.ColorTexture1D;
import com.metsci.glimpse.painter.texture.HeatMapPainter;
import com.metsci.glimpse.plot.ColorAxisPlot2D;
import com.metsci.glimpse.plot.Plot2D;
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
        ColorAxisPlot2D plot = new ColorAxisPlot2D();

        plot.setTitle(array.getName());
        plot.setAxisLabelX(array.getName() + "[]");
        plot.setAxisLabelY(array.getName() + "[][]");

        plot.lockAspectRatioXY(1.0f);

        plot.getCrosshairPainter().showSelectionBox(false);

        HeatMapPainter painter = new HeatMapPainter(plot.getAxisZ());

        ColorTexture1D colors = new ColorTexture1D(1024);
        colors.setColorGradient(ColorGradients.gray);

        int dim0 = array.getSize(0);
        int dim1 = array.getSize(1);

        FloatTextureProjected2D texture = new FloatTextureProjected2D(dim0, dim1);

        Projection projection = new FlatProjection(0, dim0, 0, dim1);
        texture.setProjection(projection);
        texture.setData(array.toFloats());

        painter.setData(texture);
        painter.setColorScale(colors);

        plot.addPainter(painter, Plot2D.DATA_LAYER);
        plot.setColorScale(painter.getColorScale());

        plot.getAxisX().setMin(0);
        plot.getAxisX().setMax(dim0);
        plot.getAxisY().setMin(0);
        plot.getAxisY().setMax(dim1);

        canvas.addLayout(plot);
    }
}

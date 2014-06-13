package testplugin.plugins.line1d;

import testplugin.plugins.SimplePlugin1D;
import testplugin.views.Array1D;
import testplugin.views.Array1DPlot;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public class LinePlugin extends SimplePlugin1D {
    public LinePlugin() {
        super("Line", null);
    }

    @Override
    protected void installLayout(GlimpseCanvas canvas, Array1D array) {
        LinePainter painter = new LinePainter();
        painter.setData(array);
        GlimpseLayout layout = new Array1DPlot(painter, array);
        canvas.addLayout(layout);
    }
}

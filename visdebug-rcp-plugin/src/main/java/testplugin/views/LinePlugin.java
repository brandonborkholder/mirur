package testplugin.views;

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

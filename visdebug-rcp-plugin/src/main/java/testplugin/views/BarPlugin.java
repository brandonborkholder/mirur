package testplugin.views;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public class BarPlugin extends SimplePlugin1D {
    public BarPlugin() {
        super("Bar", null);
    }

    @Override
    protected void installLayout(GlimpseCanvas canvas, Array1D array) {
        VerticalBarPainter painter = new VerticalBarPainter();
        painter.setData(array);
        GlimpseLayout layout = new Array1DPlot(painter, array);
        canvas.addLayout(layout);
    }
}

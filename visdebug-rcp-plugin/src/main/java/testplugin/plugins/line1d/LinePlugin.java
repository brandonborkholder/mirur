package testplugin.plugins.line1d;

import testplugin.plugins.SimplePlugin1D;
import testplugin.views.Array1D;

import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;

public class LinePlugin extends SimplePlugin1D {
    public LinePlugin() {
        super("Line", null);
    }

    @Override
    protected GlimpseDataPainter2D createPainter(Array1D array) {
        LinePainter painter = new LinePainter();
        painter.setData(array);
        return painter;
    }
}

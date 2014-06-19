package testplugin.plugins.bar1d;

import testplugin.plugins.SimplePlugin1D;
import testplugin.views.Array1D;

import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;

public class BarPlugin extends SimplePlugin1D {
    public BarPlugin() {
        super("Bar", null);
    }

    @Override
    protected GlimpseDataPainter2D createPainter(Array1D array) {
        VerticalBarPainter painter = new VerticalBarPainter();
        painter.setData(array);
        return painter;
    }
}

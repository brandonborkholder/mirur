package mirur.plugins.line1d;

import mirur.core.Array1D;
import mirur.plugins.SimplePlugin1D;

import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;

public class LineView extends SimplePlugin1D {
    public LineView() {
        super("Line", null);
    }

    @Override
    protected GlimpseDataPainter2D createPainter(Array1D array) {
        LinePainter painter = new LinePainter();
        painter.setData(array);
        return painter;
    }
}

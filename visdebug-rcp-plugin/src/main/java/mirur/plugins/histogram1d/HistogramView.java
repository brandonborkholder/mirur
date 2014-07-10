package mirur.plugins.histogram1d;

import mirur.core.Array1D;
import mirur.plugins.SimplePlugin1D;

public class HistogramView extends SimplePlugin1D {
    public HistogramView() {
        super("Histogram", null);
    }

    @Override
    protected HistogramPainter createPainter(Array1D array) {
        HistogramPainter painter = new HistogramPainter();
        painter.setData(array.toFloats());
        return painter;
    }
}

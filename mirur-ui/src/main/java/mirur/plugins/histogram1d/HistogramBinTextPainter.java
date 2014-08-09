package mirur.plugins.histogram1d;

import mirur.core.Array1D;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.info.SimpleTextPainter;

public class HistogramBinTextPainter extends SimpleTextPainter {
    private final Axis2D srcAxis;
    private HistogramPainter histPainter;
    private Array1D array;
    private double lastBin;

    public HistogramBinTextPainter(Axis2D srcAxis) {
        this.srcAxis = srcAxis;
    }

    public void setHistogramPainter(Array1D array, HistogramPainter painter) {
        this.array = array;
        histPainter = painter;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        if (array == null) {
            return;
        }

        double selected = srcAxis.getAxisX().getSelectionCenter();
        double bin = histPainter.getBin(selected);
        if (bin != lastBin) {
            int count = histPainter.getCount(selected);
            String text = format(bin, histPainter.getBinSize(), count);
            setText(text);

            lastBin = bin;
        }

        super.paintTo(context);
    }

    private String format(double bin, double binWidth, int count) {
        return String.format("%s[%d] has %d values in [%g, %g)", array.getName(), array.getSize(), count, bin, bin + binWidth);
    }
}

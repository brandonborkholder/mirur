package mirur.plugins.histogram1d;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;
import it.unimi.dsi.fastutil.floats.Float2IntMap;
import it.unimi.dsi.fastutil.floats.Float2IntOpenHashMap;
import mirur.core.AbstractArray1dVisitor;

public class HistogramVisitor extends AbstractArray1dVisitor {
    private final double min;
    private final double max;
    private double step;
    private Float2IntOpenHashMap counts;

    public HistogramVisitor(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public float getBinWidth() {
        return (float) step;
    }

    public Float2IntMap getCounts() {
        return counts;
    }

    @Override
    protected void start(int size) {
        step = (max - min) / floor(sqrt(size)) * 1.01;
        counts = new Float2IntOpenHashMap(size);
    }

    @Override
    protected void visit(int i, float v) {
        visit(i, (double) v);
    }

    @Override
    protected void visit(int i, double v) {
        if (isInfinite(v) || isNaN(v)) {
            // nop
        } else {
            float k = (float) (floor((v - min) / step) * step + min);
            counts.addTo(k, 1);
        }
    }
}

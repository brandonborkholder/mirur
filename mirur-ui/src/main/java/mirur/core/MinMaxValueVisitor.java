package mirur.core;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MinMaxValueVisitor implements ArrayElementVisitor {
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public void visit(double v) {
        min = min(min, v);
        max = max(max, v);
    }

    @Override
    public void visit(long v) {
        visit((double) v);
    }

    @Override
    public void visit(float v) {
        visit((double) v);
    }

    @Override
    public void visit(int v) {
        visit((double) v);
    }

    @Override
    public void visit(short v) {
        visit((double) v);
    }

    @Override
    public void visit(char v) {
        visit((double) v);
    }

    @Override
    public void visit(byte v) {
        visit((double) v);
    }

    @Override
    public void visit(boolean v) {
        visit(v ? 1.0 : 0.0);
    }
}

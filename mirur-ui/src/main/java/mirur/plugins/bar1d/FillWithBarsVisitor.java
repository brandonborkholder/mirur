package mirur.plugins.bar1d;

import java.nio.FloatBuffer;

import mirur.core.AbstractArray1dVisitor;

public class FillWithBarsVisitor extends AbstractArray1dVisitor {
    private final FloatBuffer buffer;

    public FillWithBarsVisitor(FloatBuffer buffer) {
        this.buffer = buffer;
    }

    public static final int requiredSpace(int numValues) {
        return numValues * 8;
    }

    @Override
    protected void visit(int i, double v) {
        visit(i, (float) v);
    }

    @Override
    protected void visit(int i, float v) {
        buffer.put(i - 0.5f);
        buffer.put(v);
        buffer.put(i - 0.5f);
        buffer.put(0);
        buffer.put(i + 0.5f);
        buffer.put(v);
        buffer.put(i + 0.5f);
        buffer.put(0);
    }
}

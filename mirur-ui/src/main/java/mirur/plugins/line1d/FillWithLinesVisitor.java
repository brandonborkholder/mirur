package mirur.plugins.line1d;

import java.nio.FloatBuffer;

import mirur.core.AbstractArray1dVisitor;

public class FillWithLinesVisitor extends AbstractArray1dVisitor {
    private final FloatBuffer buffer;

    public FillWithLinesVisitor(FloatBuffer buffer) {
        this.buffer = buffer;
    }

    public static final int requiredSpace(int numValues) {
        return numValues * 2;
    }

    @Override
    protected void visit(int i, double v) {
        visit(i, (float) v);
    }

    @Override
    protected void visit(int i, float v) {
        buffer.put(i);
        buffer.put(v);
    }
}

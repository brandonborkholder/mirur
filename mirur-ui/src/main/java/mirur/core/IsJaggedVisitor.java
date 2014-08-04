package mirur.core;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class IsJaggedVisitor extends AbstractArrayVisitor {
    private int minSize1;
    private int maxSize1;

    public IsJaggedVisitor() {
        minSize1 = Integer.MAX_VALUE;
        maxSize1 = Integer.MIN_VALUE;
    }

    public boolean isJagged() {
        return minSize1 != maxSize1;
    }

    @Override
    public void visit(int i, double[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, long[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, float[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, int[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, short[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, char[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, byte[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, boolean[] v) {
        minSize1 = min(minSize1, v.length);
        maxSize1 = max(maxSize1, v.length);
    }

    @Override
    public void visit(int i, int j, double v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, long v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, float v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, short v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, char v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, byte v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(int i, int j, boolean v) {
        throw new UnsupportedOperationException();
    }
}

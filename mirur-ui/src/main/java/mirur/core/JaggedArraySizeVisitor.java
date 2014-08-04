package mirur.core;

import static java.lang.Math.max;

public class JaggedArraySizeVisitor extends AbstractArrayVisitor {
    private int size0;
    private int size1;

    public int getSize0() {
        return size0;
    }

    public int getSize1() {
        return size1;
    }

    @Override
    public void visit(double[][] v) {
        size0 = v.length;
        super.visit(v);
    }

    @Override
    public void visit(long[][] v) {
        size0 = v.length;
        super.visit(v);
    }

    @Override
    public void visit(float[][] v) {
        size0 = v.length;
        super.visit(v);
    }

    @Override
    public void visit(int[][] v) {
        size0 = v.length;
        super.visit(v);
    }

    @Override
    public void visit(short[][] v) {
        size0 = v.length;
        super.visit(v);
    }

    @Override
    public void visit(char[][] v) {
        size0 = v.length;
        super.visit(v);
    }

    @Override
    public void visit(byte[][] v) {
        size0 = v.length;
        super.visit(v);
    }

    @Override
    public void visit(boolean[][] v) {
        size0 = v.length;
        super.visit(v);
    }

    @Override
    public void visit(int i, double[] v) {
        size1 = max(size1, v.length);
    }

    @Override
    public void visit(int i, long[] v) {
        size1 = max(size1, v.length);
    }

    @Override
    public void visit(int i, float[] v) {
        size1 = max(size1, v.length);
    }

    @Override
    public void visit(int i, int[] v) {
        size1 = max(size1, v.length);
    }

    @Override
    public void visit(int i, short[] v) {
        size1 = max(size1, v.length);
    }

    @Override
    public void visit(int i, char[] v) {
        size1 = max(size1, v.length);
    }

    @Override
    public void visit(int i, byte[] v) {
        size1 = max(size1, v.length);
    }

    @Override
    public void visit(int i, boolean[] v) {
        size1 = max(size1, v.length);
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

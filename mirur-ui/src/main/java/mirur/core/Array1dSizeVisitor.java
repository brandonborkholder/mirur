package mirur.core;

public class Array1dSizeVisitor implements Array1dVisitor {
    private int size;

    public int getSize() {
        return size;
    }

    @Override
    public void visit(double[] v) {
        size = v.length;
    }

    @Override
    public void visit(long[] v) {
        size = v.length;
    }

    @Override
    public void visit(float[] v) {
        size = v.length;
    }

    @Override
    public void visit(int[] v) {
        size = v.length;
    }

    @Override
    public void visit(short[] v) {
        size = v.length;
    }

    @Override
    public void visit(char[] v) {
        size = v.length;
    }

    @Override
    public void visit(byte[] v) {
        size = v.length;
    }

    @Override
    public void visit(boolean[] v) {
        size = v.length;
    }
}

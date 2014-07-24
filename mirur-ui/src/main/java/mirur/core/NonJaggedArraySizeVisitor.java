package mirur.core;

public class NonJaggedArraySizeVisitor implements Array2dVisitor {
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
        size1 = v[0].length;
    }

    @Override
    public void visit(long[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(float[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(int[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(short[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(char[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(byte[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }

    @Override
    public void visit(boolean[][] v) {
        size0 = v.length;
        size1 = v[0].length;
    }
}

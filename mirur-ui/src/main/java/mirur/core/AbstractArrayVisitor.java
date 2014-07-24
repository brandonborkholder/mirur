package mirur.core;

public abstract class AbstractArrayVisitor implements Array1dVisitor, Array2dVisitor {
    @Override
    public void visit(double[] v) {
        visit(0, v);
    }

    @Override
    public void visit(long[] v) {
        visit(0, v);
    }

    @Override
    public void visit(float[] v) {
        visit(0, v);
    }

    @Override
    public void visit(int[] v) {
        visit(0, v);
    }

    @Override
    public void visit(short[] v) {
        visit(0, v);
    }

    @Override
    public void visit(char[] v) {
        visit(0, v);
    }

    @Override
    public void visit(byte[] v) {
        visit(0, v);
    }

    @Override
    public void visit(boolean[] v) {
        visit(0, v);
    }

    @Override
    public void visit(double[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(long[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(float[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(int[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(short[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(char[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(byte[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(boolean[][] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    public void visit(int i, double[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, long[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, float[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, int[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, short[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, char[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, byte[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public void visit(int i, boolean[] v) {
        for (int j = 0; j < v.length; j++) {
            visit(i, j, v[j]);
        }
    }

    public abstract void visit(int i, int j, double v);

    public abstract void visit(int i, int j, long v);

    public abstract void visit(int i, int j, float v);

    public abstract void visit(int i, int j, int v);

    public abstract void visit(int i, int j, short v);

    public abstract void visit(int i, int j, char v);

    public abstract void visit(int i, int j, byte v);

    public abstract void visit(int i, int j, boolean v);
}

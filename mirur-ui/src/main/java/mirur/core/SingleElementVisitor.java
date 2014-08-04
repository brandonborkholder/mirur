package mirur.core;

public class SingleElementVisitor extends ArrayEachElementVisitor {
    private final int I;
    private final int J;

    public SingleElementVisitor(int i, int j, ArrayElementVisitor visitor) {
        super(visitor);
        I = i;
        J = j;
    }

    @Override
    public void visit(double[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, double[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(float[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, float[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(long[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, long[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(int[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, int[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(short[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, short[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(byte[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, byte[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(char[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, char[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }

    @Override
    public void visit(boolean[][] v) {
        if (0 <= I && I < v.length) {
            visit(I, v[I]);
        }
    }

    @Override
    public void visit(int i, boolean[] v) {
        if (I == i && 0 <= J && J < v.length) {
            visit(i, J, v[J]);
        }
    }
}

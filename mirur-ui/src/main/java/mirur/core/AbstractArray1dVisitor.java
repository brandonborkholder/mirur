package mirur.core;

public abstract class AbstractArray1dVisitor implements Array1dVisitor {
    @Override
    public void visit(double[] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(long[] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(float[] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(int[] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(short[] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(char[] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(byte[] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    @Override
    public void visit(boolean[] v) {
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
    }

    protected void visit(int i, long v) {
        visit(i, (double) v);
    }

    protected void visit(int i, int v) {
        visit(i, (float) v);
    }

    protected void visit(int i, short v) {
        visit(i, (float) v);
    }

    protected void visit(int i, char v) {
        visit(i, (float) v);
    }

    protected void visit(int i, byte v) {
        visit(i, (float) v);
    }

    protected void visit(int i, boolean v) {
        visit(i, v ? 1f : 0f);
    }

    protected abstract void visit(int i, double v);

    protected abstract void visit(int i, float v);
}

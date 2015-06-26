package mirur.core;

public abstract class AbstractArray1dVisitor implements Array1dVisitor {
    protected void start(int size) {
    }

    protected void stop() {
    }

    @Override
    public void visit(double[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
        stop();
    }

    @Override
    public void visit(long[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
        stop();
    }

    @Override
    public void visit(float[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
        stop();
    }

    @Override
    public void visit(int[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
        stop();
    }

    @Override
    public void visit(short[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
        stop();
    }

    @Override
    public void visit(char[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
        stop();
    }

    @Override
    public void visit(byte[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
        stop();
    }

    @Override
    public void visit(boolean[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i++) {
            visit(i, v[i]);
        }
        stop();
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

package mirur.core;

public abstract class AbstractInterleavedVisitor implements Array1dVisitor {
    protected void start(int length) {
    }

    protected void stop() {
    }

    @Override
    public void visit(double[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(long[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(float[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(int[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(short[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(char[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(byte[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(boolean[] v) {
        start(v.length);
        for (int i = 0; i < v.length; i += 2) {
            visit(i, v[i] ? 1 : 0, v[i + 1] ? 1 : 0);
        }
        stop();
    }

    protected abstract void visit(int i, double v1, double v2);

    protected abstract void visit(int i, float v1, float v2);
}

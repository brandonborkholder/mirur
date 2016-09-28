package mirur.plugins.complex;

import mirur.core.Array1dVisitor;

public abstract class AbstractComplexVisitor implements Array1dVisitor {
    protected double[] dest;

    public double[] get() {
        return dest;
    }

    protected void start(int length) {
        dest = new double[length];
    }

    protected void stop() {
    }

    @Override
    public void visit(double[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(long[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(float[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(int[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(short[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(char[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(byte[] v) {
        start(v.length >> 1);
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
        stop();
    }

    @Override
    public void visit(boolean[] v) {
        throw new UnsupportedOperationException();
    }

    protected void visit(int i, double re, double im) {
        double v = compute(re, im);
        dest[i] = v;
    }

    protected void visit(int i, float re, float im) {
        float v = compute(re, im);
        dest[i] = v;
    }

    protected abstract float compute(float re, float im);

    protected abstract double compute(double re, double im);
}

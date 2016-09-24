package mirur.core;

public abstract class AbstractComplexVisitor implements Array1dVisitor {
    protected final AbstractArray1dVisitor inner;

    public AbstractComplexVisitor(AbstractArray1dVisitor inner) {
        this.inner = inner;
    }

    protected void start(int size) {
        inner.start(size / 2);
    }

    protected void stop() {
        inner.stop();
    }

    @Override
    public void visit(double[] v) {
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
    }

    @Override
    public void visit(long[] v) {
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
    }

    @Override
    public void visit(float[] v) {
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
    }

    @Override
    public void visit(int[] v) {
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
    }

    @Override
    public void visit(short[] v) {
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
    }

    @Override
    public void visit(char[] v) {
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
    }

    @Override
    public void visit(byte[] v) {
        for (int i = 0; i < v.length; i += 2) {
            visit(i >> 1, v[i], v[i + 1]);
        }
    }

    @Override
    public void visit(boolean[] v) {
        throw new UnsupportedOperationException();
    }

    protected void visit(int i, double re, double im) {
        double v = compute(re, im);
        inner.visit(i, v);
    }

    protected void visit(int i, float re, float im) {
        float v = compute(re, im);
        inner.visit(i, v);
    }

    protected abstract float compute(float re, float im);

    protected abstract double compute(double re, double im);
}

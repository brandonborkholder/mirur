package mirur.plugins.complex;

import mirur.core.AbstractInterleavedVisitor;

public class CplxAngleVisitor extends AbstractInterleavedVisitor {
    private double dest[];

    public double[] get() {
        return dest;
    }

    @Override
    protected void start(int length) {
        dest = new double[length >> 1];
    }

    @Override
    protected void visit(int i, double re, double im) {
        dest[i >> 1] = Math.atan2(im, re);
    }

    @Override
    protected void visit(int i, float re, float im) {
        dest[i >> 1] = Math.atan2(im, re);
    }
}

package mirur.plugins.complex;

public class CplxAngleVisitor extends AbstractComplexVisitor {
    @Override
    protected double compute(double re, double im) {
        return Math.atan2(im, re);
    }

    @Override
    protected float compute(float re, float im) {
        return (float) Math.atan2(im, re);
    }
}

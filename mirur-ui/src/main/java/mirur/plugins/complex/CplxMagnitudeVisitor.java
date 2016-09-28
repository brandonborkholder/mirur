package mirur.plugins.complex;

public class CplxMagnitudeVisitor extends AbstractComplexVisitor {
    @Override
    protected double compute(double re, double im) {
        return Math.hypot(re, im);
    }

    @Override
    protected float compute(float re, float im) {
        return (float) Math.hypot(re, im);
    }
}

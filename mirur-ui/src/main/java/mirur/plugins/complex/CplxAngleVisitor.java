package mirur.plugins.complex;

import mirur.core.AbstractComplexVisitor;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.SimpleVBO;
import mirur.plugins.line1d.FillWithLinesVisitor;

public class CplxAngleVisitor extends AbstractComplexVisitor {
    public CplxAngleVisitor(SimpleVBO vbo, DataUnitConverter dataConverter) {
        super(new FillWithLinesVisitor(vbo, dataConverter));
    }

    @Override
    protected double compute(double re, double im) {
        return Math.atan2(im, re);
    }

    @Override
    protected float compute(float re, float im) {
        return (float) Math.atan2(im, re);
    }
}

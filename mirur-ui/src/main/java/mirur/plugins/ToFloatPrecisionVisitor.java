package mirur.plugins;

import static com.google.common.primitives.Doubles.isFinite;
import static java.lang.Double.isInfinite;
import static java.lang.Math.max;
import static java.lang.Math.min;

import mirur.core.ArrayElementVisitor;
import mirur.plugins.DataUnitConverter.LinearScaleConverter;

public class ToFloatPrecisionVisitor implements ArrayElementVisitor {
    private double dMin;
    private double dMax;

    public ToFloatPrecisionVisitor() {
        dMin = Double.POSITIVE_INFINITY;
        dMax = Double.NEGATIVE_INFINITY;
    }

    public DataUnitConverter get() {
        if (isInfinite(dMin) || isInfinite(dMax)) {
            return DataUnitConverter.IDENTITY;
        } else {
            return new LinearScaleConverter(dMin, dMax);
        }
    }

    @Override
    public void visit(double v) {
        if (isFinite(v)) {
            dMin = min(dMin, v);
            dMax = max(dMax, v);
        }
    }

    @Override
    public void visit(long v) {
        dMin = min(dMin, v);
        dMax = max(dMax, v);
    }

    @Override
    public void visit(float v) {
    }

    @Override
    public void visit(int v) {
    }

    @Override
    public void visit(short v) {
    }

    @Override
    public void visit(char v) {
    }

    @Override
    public void visit(byte v) {
    }

    @Override
    public void visit(boolean v) {
    }
}

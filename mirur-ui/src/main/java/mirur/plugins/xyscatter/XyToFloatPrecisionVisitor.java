package mirur.plugins.xyscatter;

import mirur.core.AbstractInterleavedVisitor;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.ToFloatPrecisionVisitor;

public class XyToFloatPrecisionVisitor extends AbstractInterleavedVisitor {
    private ToFloatPrecisionVisitor xVisitor;
    private ToFloatPrecisionVisitor yVisitor;

    public XyToFloatPrecisionVisitor() {
        xVisitor = new ToFloatPrecisionVisitor();
        yVisitor = new ToFloatPrecisionVisitor();
    }

    public DataUnitConverter getXUnitConverter() {
        return xVisitor.get();
    }

    public DataUnitConverter getYUnitConverter() {
        return yVisitor.get();
    }

    @Override
    protected void visit(int i, double x, double y) {
        xVisitor.visit(x);
        yVisitor.visit(y);
    }

    @Override
    protected void visit(int i, float x, float y) {
        xVisitor.visit(x);
        yVisitor.visit(y);
    }
}

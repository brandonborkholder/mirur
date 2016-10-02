package mirur.plugins.xyscatter;

import static com.google.common.primitives.Doubles.isFinite;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.metsci.glimpse.axis.Axis2D;

import mirur.core.AbstractInterleavedVisitor;

public class UpdateXyAxesVisitor extends AbstractInterleavedVisitor {
    private double minX = Double.POSITIVE_INFINITY;
    private double maxX = Double.NEGATIVE_INFINITY;
    private double minY = Double.POSITIVE_INFINITY;
    private double maxY = Double.NEGATIVE_INFINITY;

    public void updateAxes(Axis2D axis) {
        if (minX == maxX) {
            maxX++;
        }

        if (minY == maxY) {
            maxY++;
        }

        axis.getAxisX().setMin(minX);
        axis.getAxisX().setMax(maxX);
        axis.getAxisY().setMin(minY);
        axis.getAxisY().setMax(maxY);
    }

    @Override
    protected void visit(int i, double x, double y) {
        if (isFinite(x)) {
            minX = min(x, minX);
            maxX = max(x, maxX);
        }
        if (isFinite(y)) {
            minY = min(y, minY);
            maxY = max(y, maxY);
        }
    }

    @Override
    protected void visit(int i, float x, float y) {
        visit(i, (double) x, (double) y);
    }
}

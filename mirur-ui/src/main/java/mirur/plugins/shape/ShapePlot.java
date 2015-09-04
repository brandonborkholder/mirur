package mirur.plugins.shape;

import java.awt.Shape;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.plot.Plot2D;
import com.metsci.glimpse.plot.SimplePlot2D;

public class ShapePlot extends SimplePlot2D {
    public ShapePlot(Shape shape) {
        setTitleHeight(0);
        setAxisSizeZ(0);

        ShapePainter painter = new ShapePainter(shape);
        addPainter(painter, Plot2D.DATA_LAYER);

        painter.adjustAxis(getAxis());

        padAxis(getAxisX());
        padAxis(getAxisY());
    }

    protected void padAxis(Axis1D axis) {
        double padding = (axis.getMax() - axis.getMin()) * 0.02;
        axis.setMin(axis.getMin() - padding);
        axis.setMax(axis.getMax() + padding);
        axis.validate();
    }
}

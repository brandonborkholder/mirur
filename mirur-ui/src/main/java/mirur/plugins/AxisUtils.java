package mirur.plugins;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.Axis2D;

public class AxisUtils {
    public static void padAxis2d(Axis2D axis) {
        padAxis(axis.getAxisX());
        padAxis(axis.getAxisY());
    }

    public static void padAxis(Axis1D axis) {
        double padding = (axis.getMax() - axis.getMin()) * 0.02;
        axis.setMin(axis.getMin() - padding);
        axis.setMax(axis.getMax() + padding);
        axis.validate();
    }
}

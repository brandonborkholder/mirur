package mirur.plugins;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.Axis2D;

import mirur.core.MinMaxFiniteValueVisitor;
import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;

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

    public static void adjustAxisToMinMax(PrimitiveArray array, Axis1D axis, DataUnitConverter unitConverter) {
        MinMaxFiniteValueVisitor minMaxVisitor = VisitArray.visit(array.getData(), new MinMaxFiniteValueVisitor());
        double min = minMaxVisitor.getMin();
        double max = minMaxVisitor.getMax();

        if (min == max) {
            max++;
        }

        axis.setMin(unitConverter.data2painter(min));
        axis.setMax(unitConverter.data2painter(max));
        axis.validate();
    }
}

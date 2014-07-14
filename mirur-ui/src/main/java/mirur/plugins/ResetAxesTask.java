package mirur.plugins;

import com.metsci.glimpse.axis.Axis2D;

public class ResetAxesTask implements Runnable {
    private final Axis2D axis;

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    public ResetAxesTask(Axis2D axis) {
        this.axis = axis;
        this.minX = axis.getMinX();
        this.maxX = axis.getMaxX();
        this.minY = axis.getMinY();
        this.maxY = axis.getMaxY();
    }

    @Override
    public void run() {
        axis.getAxisX().setMin(minX);
        axis.getAxisX().setMax(maxX);
        axis.getAxisY().setMin(minY);
        axis.getAxisY().setMax(maxY);
    }
}

package mirur.plugins.xyscatter;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.listener.AxisListener1D;

public class FixedPixelSelectionSizeListener implements AxisListener1D {
    private int selectionSize_PX;

    public FixedPixelSelectionSizeListener(int selectionSize_PX) {
        this.selectionSize_PX = selectionSize_PX;
    }

    @Override
    public void axisUpdated(Axis1D axis) {
        axis.setSelectionSize(selectionSize_PX / axis.getPixelsPerValue());
    }
}

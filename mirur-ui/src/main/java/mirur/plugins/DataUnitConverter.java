package mirur.plugins;

import com.metsci.glimpse.axis.painter.label.AxisUnitConverter;

public class DataUnitConverter implements AxisUnitConverter {
    public static final DataUnitConverter IDENTITY = new DataUnitConverter(1, 0);

    public final double scale;
    public final double translate;

    public DataUnitConverter(double scale, double translate) {
        this.scale = scale <= 0 ? 1 : scale;
        this.translate = translate;
    }

    @Override
    public double fromAxisUnits(double value) {
        return (value - translate) / scale;
    }

    @Override
    public double toAxisUnits(double value) {
        return value * scale + translate;
    }
}

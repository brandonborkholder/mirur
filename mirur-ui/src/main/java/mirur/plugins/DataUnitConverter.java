package mirur.plugins;

import com.metsci.glimpse.axis.painter.label.AxisUnitConverter;

public interface DataUnitConverter {
    final DataUnitConverter IDENTITY = new DataUnitConverter() {
        @Override
        public double data2painter(double value) {
            return value;
        };

        @Override
        public double painter2data(double value) {
            return value;
        };
    };

    double data2painter(double value);

    double painter2data(double value);

    public static class DataAxisUnitConverter implements AxisUnitConverter {
        final DataUnitConverter c;

        public DataAxisUnitConverter(DataUnitConverter c) {
            this.c = c;
        }

        @Override
        public double toAxisUnits(double value) {
            return c.painter2data(value);
        }

        @Override
        public double fromAxisUnits(double value) {
            return c.data2painter(value);
        }
    }

    public static class LinearScaleConverter implements DataUnitConverter {
        public final double translate;
        public final double scale;
        private final double invScale;

        public LinearScaleConverter(double min, double max) {
            this.scale = max - min;
            this.invScale = 1.0 / scale;
            this.translate = min;
        }

        @Override
        public double data2painter(double value) {
            return (value - translate) * invScale;
        }

        @Override
        public double painter2data(double value) {
            return value * scale + translate;
        }
    }

    public static class LogConverter implements DataUnitConverter {
        @Override
        public double data2painter(double value) {
            return Math.log(value);
        }

        @Override
        public double painter2data(double value) {
            return Math.exp(value);
        }
    }

    public static class ChainConverter implements DataUnitConverter {
        final DataUnitConverter[] converters;

        public ChainConverter(DataUnitConverter[] converters) {
            this.converters = converters;
        }

        @Override
        public double data2painter(double value) {
            for (int i = 0; i < converters.length; i++) {
                value = converters[i].data2painter(value);
            }
            return value;
        }

        @Override
        public double painter2data(double value) {
            for (int i = converters.length - 1; i >= 0; i--) {
                value = converters[i].painter2data(value);
            }
            return value;
        }
    }
}

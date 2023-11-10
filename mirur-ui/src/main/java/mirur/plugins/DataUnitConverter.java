/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © 2023 Brandon Borkholder (support@mirur.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugins;

import com.metsci.glimpse.core.axis.painter.label.AxisUnitConverter;

import mirur.plugins.heatmap2d.ScaleOperator;

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
            this.scale = (max - min == 0) ? 1 : (max - min);
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

    public static class ScaleOpConverter implements DataUnitConverter {
        final ScaleOperator op;

        public ScaleOpConverter(ScaleOperator op) {
            this.op = op;
        }

        @Override
        public double data2painter(double value) {
            return op.operate(value);
        }

        @Override
        public double painter2data(double value) {
            return op.unoperate(value);
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

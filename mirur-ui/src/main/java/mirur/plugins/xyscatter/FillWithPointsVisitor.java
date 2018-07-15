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
package mirur.plugins.xyscatter;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;

import mirur.core.AbstractInterleavedVisitor;
import mirur.plugins.DataUnitConverter;

public class FillWithPointsVisitor extends AbstractInterleavedVisitor {
    private final DataUnitConverter xUnitConverter;
    private final DataUnitConverter yUnitConverter;
    private FloatBuffer dataBuffer;

    public FillWithPointsVisitor(DataUnitConverter xUnitConverter, DataUnitConverter yUnitConverter) {
        this.xUnitConverter = xUnitConverter;
        this.yUnitConverter = yUnitConverter;
    }

    @Override
    protected void start(int size) {
        dataBuffer = Buffers.newDirectFloatBuffer(size * 2);
    }

    public FloatBuffer getDataBuffer() {
        return dataBuffer;
    }

    @Override
    protected void visit(int i, double x, double y) {
        dataBuffer.put((float) xUnitConverter.data2painter(x)).put((float) yUnitConverter.data2painter(y));
    }

    @Override
    protected void visit(int i, float x, float y) {
        visit(i, (double) x, (double) y);
    }
}

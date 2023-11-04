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
package mirur.plugins.bar1d;

import java.nio.FloatBuffer;

import com.metsci.glimpse.core.gl.GLEditableBuffer;

import mirur.core.AbstractArray1dVisitor;
import mirur.plugins.DataUnitConverter;

public class FillWithBarsVisitor extends AbstractArray1dVisitor {
    private final GLEditableBuffer buf;
    private final DataUnitConverter unitConverter;
    private float baseline;

    public FillWithBarsVisitor(GLEditableBuffer buf, DataUnitConverter unitConverter) {
        this.buf = buf;
        this.unitConverter = unitConverter;
        baseline = (float) unitConverter.data2painter(0);
    }

    @Override
    protected void start(int size) {
        buf.ensureRemainingFloats(size * 12);
    }

    @Override
    protected void visit(int i, double v) {
        float f = (float) unitConverter.data2painter(v);
        FloatBuffer fbuf = buf.growFloats(12);
        fbuf.put(i - 0.5f).put(baseline);
        fbuf.put(i - 0.5f).put(f);
        fbuf.put(i + 0.5f).put(f);
        fbuf.put(i - 0.5f).put(baseline);
        fbuf.put(i + 0.5f).put(baseline);
        fbuf.put(i + 0.5f).put(f);
    }

    @Override
    protected void visit(int i, float v) {
        visit(i, (double) v);
    }
}

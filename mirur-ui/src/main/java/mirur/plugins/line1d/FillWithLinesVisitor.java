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
package mirur.plugins.line1d;

import com.metsci.glimpse.core.support.shader.line.LinePath;

import mirur.core.AbstractArray1dVisitor;
import mirur.plugins.DataUnitConverter;

public class FillWithLinesVisitor extends AbstractArray1dVisitor {
    private final DataUnitConverter unitConverter;
    private LinePath path;
    private boolean first;

    public FillWithLinesVisitor(DataUnitConverter unitConverter) {
        this.unitConverter = unitConverter;
    }

    public LinePath getPath() {
        return path;
    }

    @Override
    protected void start(int size) {
        path = new LinePath(size, 1);
        first = true;
    }

    @Override
    protected void visit(int i, double v) {
        float y = (float) unitConverter.data2painter(v);

        if (first) {
            path.moveTo(i, y);
            first = false;
        } else {
            path.lineTo(i, y);
        }
    }

    @Override
    protected void visit(int i, float v) {
        visit(i, (double) v);
    }
}

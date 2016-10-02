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

import javax.media.opengl.GL;

import mirur.core.AbstractInterleavedVisitor;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.SimpleVBO;

public class FillWithPointsVisitor extends AbstractInterleavedVisitor {
    private final SimpleVBO vbo;
    private final DataUnitConverter xUnitConverter;
    private final DataUnitConverter yUnitConverter;

    public FillWithPointsVisitor(SimpleVBO vbo, DataUnitConverter xUnitConverter, DataUnitConverter yUnitConverter) {
        this.vbo = vbo;
        this.xUnitConverter = xUnitConverter;
        this.yUnitConverter = yUnitConverter;
    }

    @Override
    protected void start(int size) {
        vbo.allocate(size);
        vbo.begin(GL.GL_POINTS);
    }

    @Override
    protected void stop() {
        vbo.end();
    }

    @Override
    protected void visit(int i, double x, double y) {
        vbo.add((float) xUnitConverter.data2painter(x), (float) yUnitConverter.data2painter(y));
    }

    @Override
    protected void visit(int i, float x, float y) {
        vbo.add((float) xUnitConverter.data2painter(x), (float) yUnitConverter.data2painter(y));
    }
}

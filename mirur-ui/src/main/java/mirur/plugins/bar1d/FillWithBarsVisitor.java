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

import javax.media.opengl.GL;

import mirur.core.AbstractArray1dVisitor;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.SimpleVBO;

public class FillWithBarsVisitor extends AbstractArray1dVisitor {
    private final SimpleVBO vbo;
    private final DataUnitConverter unitConverter;

    public FillWithBarsVisitor(SimpleVBO vbo, DataUnitConverter unitConverter) {
        this.vbo = vbo;
        this.unitConverter = unitConverter;
    }

    @Override
    protected void start(int size) {
        vbo.allocate(size * 8);
        vbo.begin(GL.GL_TRIANGLE_STRIP);
    }

    @Override
    protected void stop() {
        vbo.end();
    }

    @Override
    protected void visit(int i, double v) {
        float f = (float) unitConverter.data2painter(v);
        vbo.add(i - 0.5f, f);
        vbo.add(i - 0.5f, 0);
        vbo.add(i + 0.5f, f);
        vbo.add(i + 0.5f, 0);
    }

    @Override
    protected void visit(int i, float v) {
        float f = (float) unitConverter.data2painter(v);
        vbo.add(i - 0.5f, f);
        vbo.add(i - 0.5f, 0);
        vbo.add(i + 0.5f, f);
        vbo.add(i + 0.5f, 0);
    }
}
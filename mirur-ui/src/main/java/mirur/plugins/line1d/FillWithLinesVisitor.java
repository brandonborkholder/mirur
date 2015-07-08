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

import javax.media.opengl.GL;

import mirur.core.AbstractArray1dVisitor;
import mirur.plugins.SimpleVBO;

public class FillWithLinesVisitor extends AbstractArray1dVisitor {
    private final SimpleVBO vbo;

    public FillWithLinesVisitor(SimpleVBO vbo) {
        this.vbo = vbo;
    }

    @Override
    protected void start(int size) {
        vbo.allocate(size * 2);
        vbo.begin(GL.GL_LINE_STRIP);
    }

    @Override
    protected void stop() {
        vbo.end();
    }

    @Override
    protected void visit(int i, double v) {
        visit(i, (float) v);
    }

    @Override
    protected void visit(int i, float v) {
        vbo.add(i, v);
    }
}

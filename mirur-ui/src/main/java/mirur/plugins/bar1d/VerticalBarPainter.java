/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
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
package mirur.plugins.bar1d;

import static com.metsci.glimpse.core.gl.util.GLUtils.disableBlending;
import static com.metsci.glimpse.core.gl.util.GLUtils.enableStandardBlending;
import static com.metsci.glimpse.core.support.color.GlimpseColor.getBlack;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.metsci.glimpse.core.context.GlimpseContext;
import com.metsci.glimpse.core.gl.GLEditableBuffer;
import com.metsci.glimpse.core.painter.base.GlimpsePainterBase;
import com.metsci.glimpse.core.support.settings.LookAndFeel;
import com.metsci.glimpse.core.support.shader.triangle.FlatColorProgram;

import mirur.core.Array1D;
import mirur.core.VisitArray;
import mirur.plugin.painterview.MirurLAF;
import mirur.plugins.DataUnitConverter;

public class VerticalBarPainter extends GlimpsePainterBase {
    private FlatColorProgram prog;
    private GLEditableBuffer buf;
    private float[] color;

    public VerticalBarPainter(Array1D array, DataUnitConverter unitConverter) {
        prog = new FlatColorProgram();
        buf = new GLEditableBuffer(GL.GL_STATIC_DRAW, 0);
        color = getBlack();

        VisitArray.visit1d(array.getData(), new FillWithBarsVisitor(buf, unitConverter));
    }

    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);
        color = laf.getColor(MirurLAF.DATA_COLOR);
    }

    @Override
    protected void doPaintTo(GlimpseContext context) {
        GL3 gl = getGL3(context);
        enableStandardBlending(gl);
        prog.begin(gl);
        try {
            prog.setAxisOrtho(gl, getAxis2D(context));
            prog.setColor(gl, color);
            prog.draw(gl, GL.GL_TRIANGLES, buf, 0, buf.sizeFloats() / 2);
        } finally {
            prog.end(gl);
            disableBlending(gl);
        }
    }

    @Override
    protected void doDispose(GlimpseContext context) {
        GL3 gl = getGL3(context);
        prog.dispose(gl);
        buf.dispose(gl);
    }
}

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

import static com.metsci.glimpse.support.color.GlimpseColor.getBlack;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

import mirur.core.Array1D;
import mirur.core.VisitArray;
import mirur.plugin.painterview.MirurLAF;
import mirur.plugins.SimpleVBO;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.support.settings.LookAndFeel;

public class LinePainter extends GlimpseDataPainter2D {
    private SimpleVBO vbo = new SimpleVBO();
    private float[] color = getBlack();

    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);

        color = laf.getColor(MirurLAF.DATA_COLOR);
    }

    public void setData(Array1D data) {
        VisitArray.visit1d(data.getData(), new FillWithLinesVisitor(vbo));
    }

    @Override
    public void paintTo(GL2 gl, GlimpseBounds bounds, Axis2D axis) {
        gl.glColor4fv(color, 0);
        gl.glLineWidth(2);
        vbo.draw(gl);

        gl.glPointSize(5);
        vbo.draw(gl, GL.GL_POINTS);
    }

    @Override
    protected void dispose(GLContext context) {
        super.dispose(context);
        vbo.destroy(context.getGL().getGL2());
    }
}

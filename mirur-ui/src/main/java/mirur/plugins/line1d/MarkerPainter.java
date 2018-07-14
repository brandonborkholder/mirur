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
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.gl2.GLUT;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.base.GlimpsePainterBase;
import com.metsci.glimpse.support.color.GlimpseColor;

public class MarkerPainter extends GlimpsePainterBase {
    private static final GLUT glut = new GLUT();

    private final String text;
    private final float position;

    private float[] color;

    public MarkerPainter(String text, float position) {
        this.text = text;
        this.position = position;
        color = GlimpseColor.getBlue();
    }

    @Override
    protected void doPaintTo( GlimpseContext context )
    {
        GL2 gl = context.getGL().getGL2();

        gl.glColor3fv(color, 0);
        gl.glLineWidth(2);

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(axis.getMin(), axis.getMax(), -0.5, bounds.getHeight() - 1 + 0.5f, -1, 1);

        gl.glRasterPos2f((float) (position + 10.0 / axis.getPixelsPerValue()), bounds.getHeight() - 12);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, text);

        gl.glBegin(GL.GL_LINE_STRIP);
        gl.glVertex2f(position, 0);
        gl.glVertex2f(position, bounds.getHeight());
        gl.glEnd();
    }
}

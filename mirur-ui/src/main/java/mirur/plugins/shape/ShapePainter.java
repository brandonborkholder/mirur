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
package mirur.plugins.shape;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import org.jogamp.glg2d.impl.AbstractShapeHelper;
import org.jogamp.glg2d.impl.gl2.FastLineVisitor;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.support.color.GlimpseColor;

public class ShapePainter extends GlimpseDataPainter2D {
    final Shape shape;
    final FastLineVisitor visitor;
    final Rectangle2D bounds;

    public ShapePainter(Shape shape) {
        this.shape = shape;
        bounds = shape.getBounds2D();
        visitor = new FastLineVisitor();
        visitor.setNumCurveSteps(100);
    }

    @Override
    public void paintTo(GL2 gl, GlimpseBounds bounds, Axis2D axis) {
        double maxY = this.bounds.getMaxY();

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glTranslatef(0, (float) maxY, 0);
        gl.glScalef(1, -1, 1);

        gl.glColor4fv(GlimpseColor.getBlack(), 0);
        visitor.setGLContext(gl);
        visitor.setStroke(new BasicStroke(2));
        AbstractShapeHelper.visitShape(shape, visitor);

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glPopMatrix();
    }

    public void adjustAxis(Axis2D axis) {
        axis.getAxisX().setMin(bounds.getMinX());
        axis.getAxisX().setMax(bounds.getMaxX());
        axis.getAxisY().setMin(bounds.getMinY());
        axis.getAxisY().setMax(bounds.getMaxY());
    }
}

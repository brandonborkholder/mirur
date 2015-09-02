package mirur.plugins.line1d;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.gl2.GLUT;
import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.base.GlimpsePainter1D;
import com.metsci.glimpse.painter.info.AnnotationPainter.AnnotationFont;
import com.metsci.glimpse.support.color.GlimpseColor;

public class MarkerPainter extends GlimpsePainter1D {
    private static final GLUT glut = new GLUT();

    private final String text;
    private final float position;

    private AnnotationFont font;
    private float[] color;

    public MarkerPainter(String text, float position) {
        this.text = text;
        this.position = position;
        color = GlimpseColor.getBlue();
        font = AnnotationFont.Helvetical_10;
    }

    @Override
    public void paintTo(GlimpseContext context, GlimpseBounds bounds, Axis1D axis) {
        GL2 gl = context.getGL().getGL2();

        gl.glColor3fv(color, 0);
        gl.glLineWidth(2);

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(axis.getMin(), axis.getMax(), -0.5, bounds.getHeight() - 1 + 0.5f, -1, 1);

        gl.glRasterPos2f(position + 10, bounds.getHeight() - font.getHeight() - 2);
        glut.glutBitmapString(font.getFont(), text);

        gl.glBegin(GL.GL_LINE_STRIP);
        gl.glVertex2f(position, 0);
        gl.glVertex2f(position, bounds.getHeight());
        gl.glEnd();
    }
}

package mirur.plugins.line1d;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;
import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.painter.info.AnnotationPainter.AnnotationFont;
import com.metsci.glimpse.support.color.GlimpseColor;

public class MarkerPainter extends GlimpseDataPainter2D {
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
    public void paintTo(GL2 gl, GlimpseBounds bounds, Axis2D axis) {
        gl.glColor3fv(color, 0);

        float posX = position;
        float posY = (float) axis.getMaxY();

        posX += 10f / axis.getAxisX().getPixelsPerValue();
        posY -= font.getHeight() / axis.getAxisY().getPixelsPerValue();

        gl.glRasterPos2f(posX, posY);
        glut.glutBitmapString(font.getFont(), text);
    }
}

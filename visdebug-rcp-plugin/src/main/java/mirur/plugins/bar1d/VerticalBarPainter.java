package mirur.plugins.bar1d;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import mirur.core.Array1D;
import mirur.plugin.SimpleGLBuffer;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.support.color.GlimpseColor;

public class VerticalBarPainter extends GlimpseDataPainter2D {
    private SimpleGLBuffer dataBuffer = new SimpleGLBuffer();
    private float[] color = GlimpseColor.getBlack();

    public void setColor(float[] rgba) {
        color = rgba.clone();
    }

    public void setData(Array1D data) {
        float[] values = data.toFloats();
        int numFloats = values.length * 8;
        FloatBuffer buffer = dataBuffer.getBuffer(numFloats);

        for (int i = 0; i < values.length; i++) {
            buffer.put(i - 0.5f);
            buffer.put(values[i]);
            buffer.put(i - 0.5f);
            buffer.put(0);
            buffer.put(i + 0.5f);
            buffer.put(values[i]);
            buffer.put(i + 0.5f);
            buffer.put(0);
        }
        buffer.flip();
        dataBuffer.setDirty();
    }

    @Override
    public void paintTo(GL2 gl, GlimpseBounds bounds, Axis2D axis) {
        dataBuffer.prepareDraw(gl);

        gl.glColor4fv(color, 0);
        dataBuffer.draw(gl, GL.GL_TRIANGLE_STRIP);
    }
}

package mirur.plugins.bar1d;

import static com.metsci.glimpse.support.color.GlimpseColor.getBlack;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import mirur.core.Array1D;
import mirur.core.VisitArray;
import mirur.plugin.painterview.MirurLAF;
import mirur.plugins.SimpleGLBuffer;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.support.settings.LookAndFeel;

public class VerticalBarPainter extends GlimpseDataPainter2D {
    private SimpleGLBuffer dataBuffer = new SimpleGLBuffer();
    private float[] color = getBlack();

    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);

        color = laf.getColor(MirurLAF.DATA_COLOR);
    }

    public void setData(Array1D data) {
        int numValues = data.getSize();
        int requiredFloats = FillWithBarsVisitor.requiredSpace(numValues);
        FloatBuffer buffer = dataBuffer.getBuffer(requiredFloats);

        VisitArray.visit1d(data.getData(), new FillWithBarsVisitor(buffer));

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

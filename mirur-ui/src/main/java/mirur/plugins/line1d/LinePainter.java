package mirur.plugins.line1d;

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

public class LinePainter extends GlimpseDataPainter2D {
    private SimpleGLBuffer dataBuffer = new SimpleGLBuffer();
    private float[] color = getBlack();

    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);

        color = laf.getColor(MirurLAF.DATA_COLOR);
    }

    public void setData(Array1D data) {
        int numValues = data.getSize();
        int requiredFloats = FillWithLinesVisitor.requiredSpace(numValues);
        FloatBuffer buffer = dataBuffer.getBuffer(requiredFloats);

        VisitArray.visit1d(data.getData(), new FillWithLinesVisitor(buffer));

        buffer.flip();
        dataBuffer.setDirty();
    }

    @Override
    public void paintTo(GL2 gl, GlimpseBounds bounds, Axis2D axis) {
        dataBuffer.prepareDraw(gl);

        gl.glColor4fv(color, 0);
        gl.glLineWidth(2);
        dataBuffer.draw(gl, GL.GL_LINE_STRIP);

        gl.glPointSize(5);
        dataBuffer.draw(gl, GL.GL_POINTS);
    }
}

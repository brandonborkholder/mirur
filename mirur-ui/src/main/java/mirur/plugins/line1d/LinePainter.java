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

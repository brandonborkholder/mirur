package mirur.plugins.histogram1d;

import static com.metsci.glimpse.support.color.GlimpseColor.getBlack;
import it.unimi.dsi.fastutil.floats.Float2IntMap;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLPointerFunc;

import mirur.plugin.MirurLAF;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.support.settings.LookAndFeel;

public class HistogramPainter extends com.metsci.glimpse.painter.plot.HistogramPainter {
    private Float2IntMap counts;
    private float[] borderColor = getBlack();

    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);
        setColor(laf.getColor(MirurLAF.DATA_COLOR));
        borderColor = laf.getColor(MirurLAF.DATA_BORDER_COLOR);
    }

    @Override
    public void setData(Float2IntMap counts, int totalCount, float binSize) {
        this.counts = null;
        super.setData(counts, totalCount, binSize);
        this.counts = counts;
    }

    public int getCount(double value) {
        double binValue = getBin(value);
        if (getMinX() <= binValue && binValue <= getMaxX()) {
            return counts.get((float) binValue);
        } else {
            return 0;
        }
    }

    public double getBin(double value) {
        return getBin(value, getBinSize(), getBinStart());
    }

    @Override
    public void paintTo(GL2 gl, GlimpseBounds bounds, Axis2D axis) {
        super.paintTo(gl, bounds, axis);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferHandle[0]);
        gl.glVertexPointer(2, GL.GL_FLOAT, 8, 0);
        gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);

        gl.glColor4fv(borderColor, 0);
        gl.glLineWidth(1);

        gl.glDrawArrays(GL.GL_LINES, 0, dataSize * 4);
    }
}
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
package mirur.plugins.histogram;

import static com.metsci.glimpse.gl.util.GLUtils.disableBlending;
import static com.metsci.glimpse.gl.util.GLUtils.enableStandardBlending;
import static com.metsci.glimpse.support.color.GlimpseColor.getBlack;
import static com.metsci.glimpse.support.shader.line.LineUtils.ppvAspectRatio;

import javax.media.opengl.GL3;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseBounds;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.support.settings.LookAndFeel;
import com.metsci.glimpse.support.shader.line.LineJoinType;
import com.metsci.glimpse.support.shader.line.LinePath;
import com.metsci.glimpse.support.shader.line.LineProgram;
import com.metsci.glimpse.support.shader.line.LineStyle;

import it.unimi.dsi.fastutil.floats.Float2IntMap;
import mirur.plugin.painterview.MirurLAF;

public class HistogramPainter extends com.metsci.glimpse.painter.plot.HistogramPainter {
    private Float2IntMap counts;

    private LineProgram prog;
    private LinePath path;
    private LineStyle style;

    public HistogramPainter(Float2IntMap counts, float binStart, float binSize) {
        prog = new LineProgram();
        path = new LinePath();
        style = new LineStyle();
        style.rgba = getBlack();
        style.feather_PX = 0;
        style.joinType = LineJoinType.JOIN_MITER;
        style.thickness_PX = 1;

        this.binStart = binStart;
        this.counts = counts;
        setData(counts, 1, binSize);
    }

    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);
        setColor(laf.getColor(MirurLAF.DATA_COLOR));
        style.rgba = laf.getColor(MirurLAF.DATA_BORDER_COLOR);
    }

    @Override
    public void setData(Float2IntMap counts, int totalCount, float binSize) {
        super.setData(counts, totalCount, binSize);

        final float denom = (asDensity) ? (binSize * totalCount) : totalCount;
        boolean first = true;
        for (Float2IntMap.Entry entry : counts.float2IntEntrySet()) {
            float bin = entry.getFloatKey();
            float freq = entry.getIntValue() / denom;

            if (first) {
                first = false;
                path.moveTo(bin, 0);
            } else {
                path.lineTo(bin, 0);
            }

            path.lineTo(bin, freq);
            path.lineTo(bin + binSize, freq);
            path.lineTo(bin + binSize, 0);
        }

        path.closeLoop();
    }

    public int getCount(double binValue) {
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
    public void doPaintTo(GlimpseContext context) {
        super.doPaintTo(context);

        GlimpseBounds bounds = getBounds(context);
        Axis2D axis = requireAxis2D(context);
        GL3 gl = context.getGL().getGL3();

        enableStandardBlending(gl);
        prog.begin(gl);
        try {
            prog.setViewport(gl, bounds);
            prog.setAxisOrtho(gl, axis);
            prog.setStyle(gl, style);

            prog.draw(gl, style, path, ppvAspectRatio(axis));
        } finally {
            prog.end(gl);
            disableBlending(gl);
        }
    }

    @Override
    public void doDispose(GlimpseContext context) {
        super.doDispose(context);

        GL3 gl = getGL3(context);
        prog.dispose(gl);
        path.dispose(gl);
    }
}
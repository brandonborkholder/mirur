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

import static com.metsci.glimpse.core.gl.util.GLUtils.disableBlending;
import static com.metsci.glimpse.core.gl.util.GLUtils.enableStandardBlending;
import static com.metsci.glimpse.core.support.color.GlimpseColor.getBlack;
import static com.metsci.glimpse.core.support.shader.line.LineUtils.ppvAspectRatio;

import com.jogamp.opengl.GL3;
import com.metsci.glimpse.core.axis.Axis2D;
import com.metsci.glimpse.core.context.GlimpseBounds;
import com.metsci.glimpse.core.context.GlimpseContext;
import com.metsci.glimpse.core.painter.base.GlimpsePainterBase;
import com.metsci.glimpse.core.support.settings.LookAndFeel;
import com.metsci.glimpse.core.support.shader.line.LinePath;
import com.metsci.glimpse.core.support.shader.line.LineProgram;
import com.metsci.glimpse.core.support.shader.line.LineStyle;

import mirur.core.Array1D;
import mirur.core.VisitArray;
import mirur.plugin.painterview.MirurLAF;
import mirur.plugins.DataUnitConverter;

public class LinePainter extends GlimpsePainterBase {
    private LineProgram prog;
    private LinePath path;
    private LineStyle style;

    public LinePainter(Array1D data, DataUnitConverter unitConverter) {
        prog = new LineProgram();
        path = VisitArray.visit1d(data.getData(), new FillWithLinesVisitor(unitConverter)).getPath();
        style = new LineStyle();
        style.rgba = getBlack();
        style.thickness_PX = 2;
    }

    @Override
    public void setLookAndFeel(LookAndFeel laf) {
        super.setLookAndFeel(laf);
        style.rgba = laf.getColor(MirurLAF.DATA_COLOR);
    }

    @Override
    public void doPaintTo(GlimpseContext context) {
        GlimpseBounds bounds = getBounds(context);
        Axis2D axis = requireAxis2D(context);
        GL3 gl = getGL3(context);

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
    protected void doDispose(GlimpseContext context) {
        GL3 gl = getGL3(context);
        prog.dispose(gl);
        path.dispose(gl);
    }
}

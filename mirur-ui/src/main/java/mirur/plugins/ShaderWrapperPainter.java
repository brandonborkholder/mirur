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
package mirur.plugins;

import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.base.GlimpsePainter;
import com.metsci.glimpse.painter.base.GlimpsePainterCallback;

public class ShaderWrapperPainter implements GlimpsePainterCallback {
    private volatile InitializablePipeline newPipeline;
    private InitializablePipeline pipeline;

    public ShaderWrapperPainter() {
        pipeline = InitializablePipeline.DEFAULT;
    }

    public synchronized void setPipeline(InitializablePipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public synchronized void prePaint(GlimpsePainter painter, GlimpseContext context) {
        if (newPipeline != null) {
            pipeline = newPipeline;
            newPipeline = null;
        }

        pipeline.initialize(context);
        pipeline.beginUse(context.getGL().getGL2());
    }

    @Override
    public void postPaint(GlimpsePainter painter, GlimpseContext context) {
        pipeline.endUse(context.getGL().getGL2());
    }
}

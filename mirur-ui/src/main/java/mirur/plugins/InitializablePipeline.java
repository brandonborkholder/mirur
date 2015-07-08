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

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.axis.AxisNotSetException;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.context.GlimpseTarget;
import com.metsci.glimpse.gl.shader.Pipeline;
import com.metsci.glimpse.gl.shader.Shader;
import com.metsci.glimpse.layout.GlimpseAxisLayout1D;
import com.metsci.glimpse.layout.GlimpseAxisLayout2D;

public abstract class InitializablePipeline extends Pipeline {
    public static final InitializablePipeline DEFAULT = new InitializablePipeline("default", null, null, null) {
        @Override
        protected void initialize(Axis1D axis) {
        }

        @Override
        protected void initialize(Axis2D axis) {
        }
    };

    public InitializablePipeline(String name, Shader geom, Shader vert, Shader frag) {
        super(name, geom, vert, frag);
    }

    public void initialize(GlimpseContext context) {
        GlimpseTarget target = context.getTargetStack().getTarget();
        if (target instanceof GlimpseAxisLayout2D) {
            GlimpseAxisLayout2D layout = (GlimpseAxisLayout2D) target;
            Axis2D axis = layout.getAxis(context);

            if (axis == null) {
                throw new AxisNotSetException(context);
            }

            initialize(axis);
        } else if (target instanceof GlimpseAxisLayout1D) {
            GlimpseAxisLayout1D layout = (GlimpseAxisLayout1D) target;
            Axis1D axis = layout.getAxis(context);

            if (axis == null) {
                throw new AxisNotSetException(context);
            }

            initialize(axis);
        } else {
            throw new AxisNotSetException(context);
        }
    }

    protected abstract void initialize(Axis2D axis);

    protected abstract void initialize(Axis1D axis);
}

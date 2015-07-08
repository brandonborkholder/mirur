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

import com.metsci.glimpse.gl.shader.ShaderArg;
import com.metsci.glimpse.gl.shader.ShaderType;
import com.metsci.glimpse.support.shader.geometry.SimpleShader;

public class FisheyeShader extends SimpleShader {
    private final ShaderArg radius;
    private final ShaderArg center;
    private final ShaderArg factor;

    public FisheyeShader() {
        super("fisheye", ShaderType.vertex, "shaders/fisheye.vs");

        radius = getArg("radius");
        center = getArg("center");
        factor = getArg("factor");

        factor.setValue(2);
    }

    public void setRadius(float radius) {
        this.radius.setValue(radius);
    }

    public void setMousePosition(float position) {
        this.center.setValue(position);
    }
}

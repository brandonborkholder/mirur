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

public class FisheyePipeline extends InitializablePipeline {
    private FisheyeShader shader;

    public FisheyePipeline() {
        this(new FisheyeShader());
    }

    private FisheyePipeline(FisheyeShader shader) {
        super("fisheye", null, shader, null);
        this.shader = shader;
    }

    @Override
    protected void initialize(Axis2D axis) {
        initialize(axis.getAxisX());
    }

    @Override
    protected void initialize(Axis1D axis) {
        double radius = 0.1 * (axis.getMax() - axis.getMin());
        shader.setRadius((float) radius);
        shader.setMousePosition((float) axis.getSelectionCenter());
    }
}

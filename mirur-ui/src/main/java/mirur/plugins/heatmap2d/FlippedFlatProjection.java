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
package mirur.plugins.heatmap2d;

import com.metsci.glimpse.support.projection.FlatProjection;

public class FlippedFlatProjection extends FlatProjection {
    public FlippedFlatProjection(double minX, double maxX, double minY, double maxY) {
        super(minX, maxX, minY, maxY);
    }

    @Override
    public void getVertexXY(double textureFractionX, double textureFractionY, float[] resultXY) {
        super.getVertexXY(textureFractionY, textureFractionX, resultXY);
    }

    @Override
    public double getTextureFractionX(double vertexX, double vertexY) {
        return super.getTextureFractionY(vertexX, vertexY);
    }

    @Override
    public double getTextureFractionY(double vertexX, double vertexY) {
        return super.getTextureFractionX(vertexX, vertexY);
    }
}

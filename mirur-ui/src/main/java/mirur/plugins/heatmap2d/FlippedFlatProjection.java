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

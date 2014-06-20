package testplugin.plugins;

import javax.media.opengl.GL2;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.gl.shader.Pipeline;

public class FisheyePipeline extends Pipeline {
    private FisheyeShader shader;

    public FisheyePipeline() {
        this(new FisheyeShader());
    }

    private FisheyePipeline(FisheyeShader shader) {
        super("fisheye", null, shader, null);
        this.shader = shader;
    }

    public void beginUse(GL2 gl, Axis2D axis) {
        double radius = 0.1 * (axis.getMaxX() - axis.getMinX());
        shader.setRadius((float) radius);
        shader.setMousePosition((float) axis.getAxisX().getSelectionCenter());

        super.beginUse(gl);
    }

    @Override
    public void beginUse(GL2 gl) {
        throw new UnsupportedOperationException();
    }
}

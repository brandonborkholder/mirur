package testplugin.plugins;

import testplugin.InitializablePipeline;

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

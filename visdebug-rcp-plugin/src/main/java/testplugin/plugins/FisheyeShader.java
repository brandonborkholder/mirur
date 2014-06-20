package testplugin.plugins;

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

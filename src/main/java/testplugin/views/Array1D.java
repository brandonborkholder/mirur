package testplugin.views;

import java.nio.FloatBuffer;

public interface Array1D {
    float[] toFloats();

    void pour(FloatBuffer buffer);
}

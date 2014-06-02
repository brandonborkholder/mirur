package testplugin.views;

import java.nio.FloatBuffer;

public interface Array1D {
    Object getData();

    float[] toFloats();

    void pour(FloatBuffer buffer);
}

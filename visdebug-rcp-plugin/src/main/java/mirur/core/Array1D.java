package mirur.core;

import java.nio.FloatBuffer;

public interface Array1D extends PrimitiveArray {
    float[] toFloats();

    void pour(FloatBuffer buffer);
}

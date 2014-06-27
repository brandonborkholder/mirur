package mirur.plugin;

import java.nio.FloatBuffer;

public interface Array1D extends PrimitiveArray {
    float[] toFloats();

    String format(int index);

    void pour(FloatBuffer buffer);
}

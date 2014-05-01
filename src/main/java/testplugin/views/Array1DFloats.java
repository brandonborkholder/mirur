package testplugin.views;

import java.nio.FloatBuffer;

public class Array1DFloats implements Array1D {
    private final float[] array;

    public Array1DFloats(float[] array) {
        this.array = array;
    }

    public Array1DFloats(double[] array) {
        this.array = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            this.array[i] = (float) array[i];
        }
    }

    @Override
    public float[] toFloats() {
        return array;
    }

    @Override
    public void pour(FloatBuffer buffer) {
        buffer.put(toFloats());
    }
}

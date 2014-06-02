package testplugin.views;

import java.nio.FloatBuffer;

public class Array1DFloats implements Array1D {
    private final float[] array;
    private final Object orig;

    public Array1DFloats(float[] array) {
        this.array = array;
        orig = array;
    }

    public Array1DFloats(double[] array) {
        orig = array;
        this.array = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            this.array[i] = (float) array[i];
        }
    }

    @Override
    public Object getData() {
        return orig;
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

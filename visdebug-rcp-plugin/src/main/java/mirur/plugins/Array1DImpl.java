package mirur.plugins;

import java.nio.FloatBuffer;

import mirur.plugin.Array1D;

public class Array1DImpl implements Array1D {
    private final String name;
    private final float[] array;
    private final Object orig;

    public Array1DImpl(String name, Object array) {
        this.name = name;
        this.orig = array;

        float[] data;
        if (array instanceof int[]) {
            int[] vals = (int[]) array;
            data = new float[vals.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = vals[i];
            }
        } else if (array instanceof char[]) {
            char[] vals = (char[]) array;
            data = new float[vals.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = vals[i];
            }
        } else if (array instanceof byte[]) {
            byte[] vals = (byte[]) array;
            data = new float[vals.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = vals[i];
            }
        } else if (array instanceof boolean[]) {
            boolean[] vals = (boolean[]) array;
            data = new float[vals.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = vals[i] ? 1 : 0;
            }
        } else if (array instanceof short[]) {
            short[] vals = (short[]) array;
            data = new float[vals.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = vals[i];
            }
        } else if (array instanceof double[]) {
            double[] vals = (double[]) array;
            data = new float[vals.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = (float) vals[i];
            }
        } else if (array instanceof long[]) {
            long[] vals = (long[]) array;
            data = new float[vals.length];
            for (int i = 0; i < data.length; i++) {
                data[i] = vals[i];
            }
        } else if (array instanceof float[]) {
            data = (float[]) array;
        } else {
            throw new AssertionError("Forget something?");
        }

        this.array = data;
    }

    @Override
    public String getSignature() {
        return orig.getClass().getSimpleName();
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumDimensions() {
        return 1;
    }

    @Override
    public int getSize(int dimension) {
        return array.length;
    }

    @Override
    public String format(int index) {
        // TODO Not implemented yet...
        throw new AssertionError("Not implemented yet...");
    }
}

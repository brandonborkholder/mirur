package testplugin.views;

public interface DebugViewListener {
    void inspect(int[] data);

    void inspect(float[] data);

    void inspect(short[] data);

    void inspect(byte[] data);

    void inspect(double[] data);

    void inspect(long[] data);

    void inspect(char[] data);

    void clear();
}

package testplugin.views;

public class SysoutDebugViewListener implements DebugViewListener {
    @Override
    public void inspect(int[] data) {
        System.out.println("Inspecting int[]");
    }

    @Override
    public void inspect(float[] data) {
        System.out.println("Inspecting float[]");
    }

    @Override
    public void inspect(short[] data) {
        System.out.println("Inspecting short[]");
    }

    @Override
    public void inspect(byte[] data) {
        System.out.println("Inspecting byte[]");
    }

    @Override
    public void inspect(double[] data) {
        System.out.println("Inspecting double[]");
    }

    @Override
    public void inspect(long[] data) {
        System.out.println("Inspecting long[]");
    }

    @Override
    public void inspect(char[] data) {
        System.out.println("Inspecting char[]");
    }

    @Override
    public void clear() {
        System.out.println("clearing");
    }
}

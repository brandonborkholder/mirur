package testplugin.views;

public interface Array2D extends PrimitiveArray {
    float[][] toFloats();

    boolean isSquare();

    String format(int i, int j);
}

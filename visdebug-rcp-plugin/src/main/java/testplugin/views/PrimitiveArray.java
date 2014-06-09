package testplugin.views;

public interface PrimitiveArray {
    String getName();

    Object getData();

    int getNumDimensions();

    int getSize(int dimension);
}

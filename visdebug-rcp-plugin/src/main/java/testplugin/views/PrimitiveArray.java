package testplugin.views;

public interface PrimitiveArray {
    String getName();

    String getSignature();

    Object getData();

    int getNumDimensions();

    int getSize(int dimension);
}

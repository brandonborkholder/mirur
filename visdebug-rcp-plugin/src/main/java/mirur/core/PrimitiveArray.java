package mirur.core;

public interface PrimitiveArray {
    String getName();

    String getSignature();

    Object getData();

    int getNumDimensions();

    int getSize(int dimension);
}

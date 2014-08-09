package mirur.core;

public interface Array2D extends PrimitiveArray {
    boolean isJagged();

    int getMaxSize(int dimension);
}

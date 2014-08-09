package mirur.core;

public class Array1DImpl implements Array1D {
    private final String name;
    private final Object data;
    private final int size;

    public Array1DImpl(String name, Object array) {
        this.name = name;
        this.data = array;

        size = VisitArray.visit1d(array, new Array1dSizeVisitor()).getSize();
    }

    @Override
    public String getSignature() {
        return data.getClass().getSimpleName();
    }

    @Override
    public Object getData() {
        return data;
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
    public int getSize() {
        return size;
    }
}

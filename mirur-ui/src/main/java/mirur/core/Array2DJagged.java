package mirur.core;

public class Array2DJagged implements Array2D {
    private final String name;
    private final Object orig;

    private final int size0;
    private final int minSize1;
    private final int maxSize1;

    public Array2DJagged(String name, Object array) {
        this.name = name;
        this.orig = array;

        JaggedArraySizeVisitor sizeVisitor = VisitArray.visit2d(array, new JaggedArraySizeVisitor());
        size0 = sizeVisitor.getSize0();
        minSize1 = sizeVisitor.getMinSize1();
        maxSize1 = sizeVisitor.getMaxSize1();
    }

    @Override
    public String getSignature() {
        return orig.getClass().getSimpleName();
    }

    @Override
    public boolean isJagged() {
        return true;
    }

    @Override
    public Object getData() {
        return orig;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumDimensions() {
        return 2;
    }

    @Override
    public int getMaxSize(int dimension) {
        if (dimension == 0) {
            return size0;
        } else if (dimension == 1) {
            return maxSize1;
        } else {
            throw new AssertionError("No dimension: " + dimension);
        }
    }

    @Override
    public String toString() {
        return String.format("%s[%d][%d-%d]", orig.getClass().getComponentType().getComponentType(), size0, minSize1, maxSize1);
    }
}

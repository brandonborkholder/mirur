package mirur.core;

public class Array2DRectangular implements Array2D {
    private final String name;
    private final Object orig;

    private final int size0;
    private final int size1;

    public Array2DRectangular(String name, Object array) {
        this.name = name;
        this.orig = array;

        NonJaggedArraySizeVisitor sizeVisitor = VisitArray.visit(array, new NonJaggedArraySizeVisitor());
        size0 = sizeVisitor.getSize0();
        size1 = sizeVisitor.getSize1();
    }

    @Override
    public String getSignature() {
        return orig.getClass().getSimpleName();
    }

    @Override
    public boolean isJagged() {
        return false;
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
    public int getSize(int dimension) {
        if (dimension == 0) {
            return size0;
        } else if (dimension == 1) {
            return size1;
        } else {
            throw new AssertionError("No dimension: " + dimension);
        }
    }
}

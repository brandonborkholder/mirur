package mirur.core;

public class Array2DSquare implements Array2D {
    private final String name;
    private final float[][] array;
    private final Object orig;

    public Array2DSquare(String name, Object array) {
        this.name = name;
        this.orig = array;

        float[][] data;
        if (array instanceof int[][]) {
            int[][] vals = (int[][]) array;
            data = new float[vals.length][vals[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    data[i][j] = vals[i][j];
                }
            }
        } else if (array instanceof char[][]) {
            char[][] vals = (char[][]) array;
            data = new float[vals.length][vals[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    data[i][j] = vals[i][j];
                }
            }
        } else if (array instanceof byte[][]) {
            byte[][] vals = (byte[][]) array;
            data = new float[vals.length][vals[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    data[i][j] = vals[i][j];
                }
            }
        } else if (array instanceof boolean[][]) {
            boolean[][] vals = (boolean[][]) array;
            data = new float[vals.length][vals[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    data[i][j] = vals[i][j] ? 1 : 0;
                }
            }
        } else if (array instanceof short[][]) {
            short[][] vals = (short[][]) array;
            data = new float[vals.length][vals[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    data[i][j] = vals[i][j];
                }
            }
        } else if (array instanceof double[][]) {
            double[][] vals = (double[][]) array;
            data = new float[vals.length][vals[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    data[i][j] = (float) vals[i][j];
                }
            }
        } else if (array instanceof long[][]) {
            long[][] vals = (long[][]) array;
            data = new float[vals.length][vals[0].length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    data[i][j] = vals[i][j];
                }
            }
        } else if (array instanceof float[][]) {
            data = (float[][]) array;
        } else {
            throw new AssertionError("Forget something?");
        }

        this.array = data;
    }

    @Override
    public String getSignature() {
        return orig.getClass().getSimpleName();
    }

    @Override
    public boolean isSquare() {
        return true;
    }

    @Override
    public Object getData() {
        return orig;
    }

    @Override
    public float[][] toFloats() {
        return array;
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
            return array.length;
        } else if (dimension == 1) {
            return array[0].length;
        } else {
            throw new AssertionError("No dimension: " + dimension);
        }
    }
}

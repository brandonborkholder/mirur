package mirur.core;

import static mirur.core.PrimitiveTest.isPrimitiveArray1d;
import static mirur.core.PrimitiveTest.isPrimitiveArray2d;

public class VisitArray {
    public static <T extends ArrayVisitor> T visit(Object array, T visitor, int index) {
        if (isPrimitiveArray1d(array.getClass())) {
            return visit1d(array, visitor, index, index + 1);
        } else if (isPrimitiveArray2d(array.getClass())) {
            return visit2d(array, visitor);
        } else {
            throw new AssertionError();
        }
    }

    public static <T extends ArrayVisitor> T visit(Object array, T visitor, int i, int j) {
        if (isPrimitiveArray2d(array.getClass())) {
            return visit2d(array, visitor, i, i + 1, j, j + 1);
        } else {
            throw new AssertionError();
        }
    }

    private static int length(Object array) {
        if (array instanceof double[]) {
            double[] v = (double[]) array;
            return v.length;
        } else if (array instanceof long[]) {
            long[] v = (long[]) array;
            return v.length;
        } else if (array instanceof int[]) {
            int[] v = (int[]) array;
            return v.length;
        } else if (array instanceof short[]) {
            short[] v = (short[]) array;
            return v.length;
        } else if (array instanceof boolean[]) {
            boolean[] v = (boolean[]) array;
            return v.length;
        } else if (array instanceof byte[]) {
            byte[] v = (byte[]) array;
            return v.length;
        } else if (array instanceof float[]) {
            float[] v = (float[]) array;
            return v.length;
        } else if (array instanceof char[]) {
            char[] v = (char[]) array;
            return v.length;
        } else if (array instanceof Object[]) {
            Object[] v = (Object[]) array;
            return v.length;
        } else {
            throw new AssertionError("unexpected");
        }
    }

    private static int length(Object array, int idx) {
        if (array instanceof double[][]) {
            double[][] v = (double[][]) array;
            return v[idx].length;
        } else if (array instanceof long[][]) {
            long[][] v = (long[][]) array;
            return v[idx].length;
        } else if (array instanceof int[][]) {
            int[][] v = (int[][]) array;
            return v[idx].length;
        } else if (array instanceof short[][]) {
            short[][] v = (short[][]) array;
            return v[idx].length;
        } else if (array instanceof boolean[][]) {
            boolean[][] v = (boolean[][]) array;
            return v[idx].length;
        } else if (array instanceof byte[][]) {
            byte[][] v = (byte[][]) array;
            return v[idx].length;
        } else if (array instanceof float[][]) {
            float[][] v = (float[][]) array;
            return v[idx].length;
        } else if (array instanceof char[][]) {
            char[][] v = (char[][]) array;
            return v[idx].length;
        } else if (array instanceof Object[][]) {
            Object[][] v = (Object[][]) array;
            return v[idx].length;
        } else {
            throw new AssertionError("unexpected");
        }
    }

    public static <T extends ArrayVisitor> T visit(Object array, T visitor) {
        if (isPrimitiveArray1d(array.getClass())) {
            return visit1d(array, visitor);
        } else if (isPrimitiveArray2d(array.getClass())) {
            return visit2d(array, visitor);
        } else {
            throw new AssertionError();
        }
    }

    public static <T extends ArrayVisitor> T visit2d(Object array, T visitor) {
        int fromi = 0;
        int toi = length(array);
        int fromj = 0;
        // XXX assuming it's square
        int toj = length(array, 0);

        return visit2d(array, visitor, fromi, toi, fromj, toj);
    }

    public static <T extends ArrayVisitor> T visit2d(Object array, T visitor, int fromi, int toi, int fromj, int toj) {
        if (array instanceof double[][]) {
            double[][] v = (double[][]) array;
            for (int i = fromi; i < toi; i++) {
                visit1d(v[i], visitor, fromj, toj);
            }
        } else if (array instanceof long[][]) {
            long[][] v = (long[][]) array;
            for (int i = fromi; i < toi; i++) {
                visit1d(v[i], visitor, fromj, toj);
            }
        } else if (array instanceof int[][]) {
            int[][] v = (int[][]) array;
            for (int i = fromi; i < toi; i++) {
                visit1d(v[i], visitor, fromj, toj);
            }
        } else if (array instanceof short[][]) {
            short[][] v = (short[][]) array;
            for (int i = fromi; i < toi; i++) {
                visit1d(v[i], visitor, fromj, toj);
            }
        } else if (array instanceof boolean[][]) {
            boolean[][] v = (boolean[][]) array;
            for (int i = fromi; i < toi; i++) {
                visit1d(v[i], visitor, fromj, toj);
            }
        } else if (array instanceof byte[][]) {
            byte[][] v = (byte[][]) array;
            for (int i = fromi; i < toi; i++) {
                visit1d(v[i], visitor, fromj, toj);
            }
        } else if (array instanceof float[][]) {
            float[][] v = (float[][]) array;
            for (int i = fromi; i < toi; i++) {
                visit1d(v[i], visitor, fromj, toj);
            }
        } else if (array instanceof char[][]) {
            char[][] v = (char[][]) array;
            for (int i = fromi; i < toi; i++) {
                visit1d(v[i], visitor, fromj, toj);
            }
        } else {
            throw new AssertionError("unexpected");
        }

        return visitor;
    }

    public static <T extends ArrayVisitor> T visit1d(Object array, T visitor) {
        int len = length(array);
        return visit1d(array, visitor, 0, len);
    }

    public static <T extends ArrayVisitor> T visit1d(Object array, T visitor, int from, int to) {
        if (array instanceof double[]) {
            double[] v = (double[]) array;
            for (int i = from; i < to; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof long[]) {
            long[] v = (long[]) array;
            for (int i = from; i < to; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof int[]) {
            int[] v = (int[]) array;
            for (int i = from; i < to; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof short[]) {
            short[] v = (short[]) array;
            for (int i = from; i < to; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof boolean[]) {
            boolean[] v = (boolean[]) array;
            for (int i = from; i < to; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof byte[]) {
            byte[] v = (byte[]) array;
            for (int i = from; i < to; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof float[]) {
            float[] v = (float[]) array;
            for (int i = from; i < to; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof char[]) {
            char[] v = (char[]) array;
            for (int i = from; i < to; i++) {
                visitor.visit(i, v[i]);
            }
        } else {
            throw new AssertionError("unexpected");
        }

        return visitor;
    }
}

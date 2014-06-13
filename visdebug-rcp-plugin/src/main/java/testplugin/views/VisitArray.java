package testplugin.views;

import static testplugin.views.PrimitiveTest.isPrimitiveArray1d;
import static testplugin.views.PrimitiveTest.isPrimitiveArray2d;

public class VisitArray {
    public static ArrayVisitor visit(Object array, ArrayVisitor visitor) {
        if (isPrimitiveArray1d(array.getClass())) {
            return visit1d(array, visitor);
        } else if (isPrimitiveArray2d(array.getClass())) {
            return visit2d(array, visitor);
        } else {
            throw new AssertionError();
        }
    }

    public static ArrayVisitor visit2d(Object array, ArrayVisitor visitor) {
        if (array instanceof double[][]) {
            double[][] v = (double[][]) array;
            for (int i = 0; i < v.length; i++) {
                visit1d(v[i], visitor);
            }
        } else if (array instanceof long[][]) {
            long[][] v = (long[][]) array;
            for (int i = 0; i < v.length; i++) {
                visit1d(v[i], visitor);
            }
        } else if (array instanceof int[][]) {
            int[][] v = (int[][]) array;
            for (int i = 0; i < v.length; i++) {
                visit1d(v[i], visitor);
            }
        } else if (array instanceof short[][]) {
            short[][] v = (short[][]) array;
            for (int i = 0; i < v.length; i++) {
                visit1d(v[i], visitor);
            }
        } else if (array instanceof boolean[][]) {
            boolean[][] v = (boolean[][]) array;
            for (int i = 0; i < v.length; i++) {
                visit1d(v[i], visitor);
            }
        } else if (array instanceof byte[][]) {
            byte[][] v = (byte[][]) array;
            for (int i = 0; i < v.length; i++) {
                visit1d(v[i], visitor);
            }
        } else if (array instanceof float[][]) {
            float[][] v = (float[][]) array;
            for (int i = 0; i < v.length; i++) {
                visit1d(v[i], visitor);
            }
        } else if (array instanceof char[][]) {
            char[][] v = (char[][]) array;
            for (int i = 0; i < v.length; i++) {
                visit1d(v[i], visitor);
            }
        } else {
            throw new AssertionError("unexpected");
        }

        return visitor;
    }

    public static ArrayVisitor visit1d(Object array, ArrayVisitor visitor) {
        if (array instanceof double[]) {
            double[] v = (double[]) array;
            for (int i = 0; i < v.length; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof long[]) {
            long[] v = (long[]) array;
            for (int i = 0; i < v.length; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof int[]) {
            int[] v = (int[]) array;
            for (int i = 0; i < v.length; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof short[]) {
            short[] v = (short[]) array;
            for (int i = 0; i < v.length; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof boolean[]) {
            boolean[] v = (boolean[]) array;
            for (int i = 0; i < v.length; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof byte[]) {
            byte[] v = (byte[]) array;
            for (int i = 0; i < v.length; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof float[]) {
            float[] v = (float[]) array;
            for (int i = 0; i < v.length; i++) {
                visitor.visit(i, v[i]);
            }
        } else if (array instanceof char[]) {
            char[] v = (char[]) array;
            for (int i = 0; i < v.length; i++) {
                visitor.visit(i, v[i]);
            }
        } else {
            throw new AssertionError("unexpected");
        }

        return visitor;
    }
}

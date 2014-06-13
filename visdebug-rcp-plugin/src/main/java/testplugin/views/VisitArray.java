package testplugin.views;

public class VisitArray {
    public static ArrayVisitor visit(Object array, ArrayVisitor visitor) {
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

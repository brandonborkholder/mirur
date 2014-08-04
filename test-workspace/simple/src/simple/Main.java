package simple;

import java.util.ArrayList;
import java.util.List;

public class Main {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        double[] double1d = new double[500];
        for (int i = 0; i < double1d.length; i++) {
            double1d[i] = v();
        }

        short[] short1d = new short[500];
        for (int i = 0; i < short1d.length; i++) {
            short1d[i] = (short) (512 * v());
        }

        long[] long1d = new long[500];
        for (int i = 0; i < long1d.length; i++) {
            long1d[i] = (long) (Long.MAX_VALUE / 2 * v());
        }

        char[] char1d = "the lazy fox jumped over the brown dog".toCharArray();

        boolean[] bool1d = new boolean[500];
        for (int i = 0; i < bool1d.length; i++) {
            bool1d[i] = v() > 0.3;
        }

        float[][] float2d = new float[500][500];
        for (int i = 0; i < float2d.length; i++) {
            for (int j = 0; j < float2d[0].length; j++) {
                float2d[i][j] = (float) v() * i;
            }
        }

        List<String> strList = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            strList.add(String.valueOf(v()));
        }

        List<Double> dblList = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            dblList.add(v());
        }

        Foo foo = new Foo(double1d, dblList);

        float[][] float2dLarge = new float[200][100000];
        for (int i = 0; i < float2dLarge.length; i++) {
            for (int j = 0; j < float2dLarge[0].length; j++) {
                float2dLarge[i][j] = (float) v() * i;
            }
        }

        float[][] jagged = new float[100][];
        for (int i = 0; i < jagged.length; i++) {
            int len = (int) (v() * 100);
            jagged[i] = new float[len];
            for (int j = 0; j < len; j++) {
                jagged[i][j] = (float) v();
            }
        }

        System.out.println("Debug here");
    }

    static double v() {
        return Math.random();
    }
}

package mirur.plugins.heatmap2d;

import java.nio.FloatBuffer;

import mirur.core.Array2dVisitor;

public class ToFloatBufferVisitor implements Array2dVisitor {
    private final FloatBuffer buf;

    public ToFloatBufferVisitor(FloatBuffer buffer) {
        buf = buffer;
    }

    private void add(float v) {
        buf.put(v);
    }

    @Override
    public void visit(double[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add((float) v[i][j]);
            }
        }
    }

    @Override
    public void visit(long[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(float[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(int[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(short[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(char[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(byte[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j]);
            }
        }
    }

    @Override
    public void visit(boolean[][] v) {
        int c = v[0].length;
        int r = v.length;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                add(v[i][j] ? 1 : 0);
            }
        }
    }
}

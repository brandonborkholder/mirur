package mirur.plugins.heatmap2d;

import java.nio.FloatBuffer;

import mirur.core.Array2dVisitor;

public class JaggedToFloatBufferVisitor implements Array2dVisitor {
    private final FloatBuffer buf;
    private final int minSize1;
    private final float fill;

    public JaggedToFloatBufferVisitor(FloatBuffer buffer, int expectedSize1, float fill) {
        buf = buffer;
        this.minSize1 = expectedSize1;
        this.fill = fill;
    }

    private void add(float v) {
        buf.put(v);
    }

    @Override
    public void visit(double[][] v) {
        int rows = v.length;

        for (int c = 0; c < minSize1; c++) {
            for (int r = 0; r < rows; r++) {
                if (c < v[r].length) {
                    add((float) v[r][c]);
                } else {
                    add(fill);
                }
            }
        }
    }

    @Override
    public void visit(long[][] v) {
        int rows = v.length;

        for (int c = 0; c < minSize1; c++) {
            for (int r = 0; r < rows; r++) {
                if (c < v[r].length) {
                    add(v[r][c]);
                } else {
                    add(fill);
                }
            }
        }
    }

    @Override
    public void visit(float[][] v) {
        int rows = v.length;

        for (int c = 0; c < minSize1; c++) {
            for (int r = 0; r < rows; r++) {
                if (c < v[r].length) {
                    add(v[r][c]);
                } else {
                    add(fill);
                }
            }
        }
    }

    @Override
    public void visit(int[][] v) {
        int rows = v.length;

        for (int c = 0; c < minSize1; c++) {
            for (int r = 0; r < rows; r++) {
                if (c < v[r].length) {
                    add(v[r][c]);
                } else {
                    add(fill);
                }
            }
        }
    }

    @Override
    public void visit(short[][] v) {
        int rows = v.length;

        for (int c = 0; c < minSize1; c++) {
            for (int r = 0; r < rows; r++) {
                if (c < v[r].length) {
                    add(v[r][c]);
                } else {
                    add(fill);
                }
            }
        }
    }

    @Override
    public void visit(char[][] v) {
        int rows = v.length;

        for (int c = 0; c < minSize1; c++) {
            for (int r = 0; r < rows; r++) {
                if (c < v[r].length) {
                    add(v[r][c]);
                } else {
                    add(fill);
                }
            }
        }
    }

    @Override
    public void visit(byte[][] v) {
        int rows = v.length;

        for (int c = 0; c < minSize1; c++) {
            for (int r = 0; r < rows; r++) {
                if (c < v[r].length) {
                    add(v[r][c]);
                } else {
                    add(fill);
                }
            }
        }
    }

    @Override
    public void visit(boolean[][] v) {
        int rows = v.length;

        for (int c = 0; c < minSize1; c++) {
            for (int r = 0; r < rows; r++) {
                if (c < v[r].length) {
                    add(v[r][c] ? 1f : 0f);
                } else {
                    add(fill);
                }
            }
        }
    }
}

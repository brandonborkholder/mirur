package mirur.core;

public interface Array2dVisitor {
    void visit(double[][] v);

    void visit(long[][] v);

    void visit(float[][] v);

    void visit(int[][] v);

    void visit(short[][] v);

    void visit(char[][] v);

    void visit(byte[][] v);

    void visit(boolean[][] v);
}

package mirur.core;

public interface ArrayVisitor {
    void visit(int i, double v);

    void visit(int i, long v);

    void visit(int i, float v);

    void visit(int i, int v);

    void visit(int i, short v);

    void visit(int i, char v);

    void visit(int i, byte v);

    void visit(int i, boolean v);
}

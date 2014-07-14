package mirur.core;

public abstract class SimpleArrayVisitor implements ArrayVisitor {
    @Override
    public void visit(int i, long v) {
        visit(i, (double) v);
    }

    @Override
    public void visit(int i, float v) {
        visit(i, (double) v);
    }

    @Override
    public void visit(int i, int v) {
        visit(i, (double) v);
    }

    @Override
    public void visit(int i, short v) {
        visit(i, (double) v);
    }

    @Override
    public void visit(int i, char v) {
        visit(i, (double) v);
    }

    @Override
    public void visit(int i, byte v) {
        visit(i, (double) v);
    }

    @Override
    public void visit(int i, boolean v) {
        visit(i, v ? 1.0 : 0.0);
    }
}

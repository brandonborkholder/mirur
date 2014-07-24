package mirur.core;

public abstract class SimpleArrayVisitor extends AbstractArrayVisitor {
    @Override
    public void visit(int i, int j, long v) {
        visit(i, j, (double) v);
    }

    @Override
    public void visit(int i, int j, float v) {
        visit(i, j, (double) v);
    }

    @Override
    public void visit(int i, int j, int v) {
        visit(i, j, (double) v);
    }

    @Override
    public void visit(int i, int j, short v) {
        visit(i, j, (double) v);
    }

    @Override
    public void visit(int i, int j, char v) {
        visit(i, j, (double) v);
    }

    @Override
    public void visit(int i, int j, byte v) {
        visit(i, j, (double) v);
    }

    @Override
    public void visit(int i, int j, boolean v) {
        visit(i, j, v ? 1.0 : 0.0);
    }
}

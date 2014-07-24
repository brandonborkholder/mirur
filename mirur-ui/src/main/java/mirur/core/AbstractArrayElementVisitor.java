package mirur.core;

public abstract class AbstractArrayElementVisitor implements ArrayElementVisitor {
    @Override
    public void visit(long v) {
        visit((double) v);
    }

    @Override
    public void visit(float v) {
        visit((double) v);
    }

    @Override
    public void visit(int v) {
        visit((double) v);
    }

    @Override
    public void visit(short v) {
        visit((double) v);
    }

    @Override
    public void visit(char v) {
        visit((double) v);
    }

    @Override
    public void visit(byte v) {
        visit((double) v);
    }

    @Override
    public void visit(boolean v) {
        visit(v ? 1.0 : 0.0);
    }
}

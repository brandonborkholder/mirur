package mirur.core;

public class ArrayEachElementVisitor extends AbstractArrayVisitor {
    private final ArrayElementVisitor visitor;

    public ArrayEachElementVisitor(ArrayElementVisitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public void visit(int i, int j, double v) {
        visitor.visit(v);
    }

    @Override
    public void visit(int i, int j, long v) {
        visitor.visit(v);
    }

    @Override
    public void visit(int i, int j, float v) {
        visitor.visit(v);
    }

    @Override
    public void visit(int i, int j, int v) {
        visitor.visit(v);
    }

    @Override
    public void visit(int i, int j, short v) {
        visitor.visit(v);
    }

    @Override
    public void visit(int i, int j, char v) {
        visitor.visit(v);
    }

    @Override
    public void visit(int i, int j, byte v) {
        visitor.visit(v);
    }

    @Override
    public void visit(int i, int j, boolean v) {
        visitor.visit(v);
    }
}

package mirur.core;

public class ElementToStringVisitor implements ArrayVisitor {
    private String text;

    @Override
    public void visit(int i, double v) {
        text = Double.toString(v);
    }

    @Override
    public void visit(int i, long v) {
        text = Long.toString(v);
    }

    @Override
    public void visit(int i, float v) {
        text = Float.toString(v);
    }

    @Override
    public void visit(int i, int v) {
        text = Integer.toString(v);
    }

    @Override
    public void visit(int i, short v) {
        text = Short.toString(v);
    }

    @Override
    public void visit(int i, char v) {
        text = Short.toString((short) v);
    }

    @Override
    public void visit(int i, byte v) {
        text = Byte.toString(v);
    }

    @Override
    public void visit(int i, boolean v) {
        text = Boolean.toString(v);
    }

    public String getText() {
        return text;
    }
}

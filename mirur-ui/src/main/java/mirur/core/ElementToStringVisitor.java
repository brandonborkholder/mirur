package mirur.core;

public class ElementToStringVisitor implements ArrayElementVisitor {
    private String text;

    @Override
    public void visit(double v) {
        text = Double.toString(v);
    }

    @Override
    public void visit(long v) {
        text = Long.toString(v);
    }

    @Override
    public void visit(float v) {
        text = Float.toString(v);
    }

    @Override
    public void visit(int v) {
        text = Integer.toString(v);
    }

    @Override
    public void visit(short v) {
        text = Short.toString(v);
    }

    @Override
    public void visit(char v) {
        text = Short.toString((short) v);
    }

    @Override
    public void visit(byte v) {
        text = Byte.toString(v);
    }

    @Override
    public void visit(boolean v) {
        text = Boolean.toString(v);
    }

    public String getText() {
        return text;
    }
}

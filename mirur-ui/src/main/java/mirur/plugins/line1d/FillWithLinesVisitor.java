package mirur.plugins.line1d;

import javax.media.opengl.GL;

import mirur.core.AbstractArray1dVisitor;
import mirur.plugins.SimpleVBO;

public class FillWithLinesVisitor extends AbstractArray1dVisitor {
    private final SimpleVBO vbo;

    public FillWithLinesVisitor(SimpleVBO vbo) {
        this.vbo = vbo;
    }

    @Override
    protected void start(int size) {
        vbo.allocate(size * 2);
        vbo.begin(GL.GL_LINE_STRIP);
    }

    @Override
    protected void stop() {
        vbo.end();
    }

    @Override
    protected void visit(int i, double v) {
        visit(i, (float) v);
    }

    @Override
    protected void visit(int i, float v) {
        vbo.add(i, v);
    }
}

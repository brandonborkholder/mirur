package mirur.plugins.bar1d;

import javax.media.opengl.GL;

import mirur.core.AbstractArray1dVisitor;
import mirur.plugins.SimpleVBO;

public class FillWithBarsVisitor extends AbstractArray1dVisitor {
    private final SimpleVBO vbo;

    public FillWithBarsVisitor(SimpleVBO vbo) {
        this.vbo = vbo;
    }

    @Override
    protected void start(int size) {
        vbo.allocate(size * 8);
        vbo.begin(GL.GL_TRIANGLE_STRIP);
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
        vbo.add(i - 0.5f, v);
        vbo.add(i - 0.5f, 0);
        vbo.add(i + 0.5f, v);
        vbo.add(i + 0.5f, 0);
    }
}

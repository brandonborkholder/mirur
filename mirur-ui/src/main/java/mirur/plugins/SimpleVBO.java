package mirur.plugins;

import static com.metsci.glimpse.gl.util.GLErrorUtils.logGLError;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLPointerFunc;

import com.jogamp.common.nio.Buffers;

public class SimpleVBO {
    private static final Logger LOGGER = Logger.getLogger(SimpleVBO.class.getName());

    private boolean bufferInitialized;
    private boolean resetBuffer;
    private int bufferHandle;

    private FloatBuffer buffer;

    private List<Marker> marks = new ArrayList<>();
    private Marker mark;

    public void destroy(GL2 gl) {
        if (bufferInitialized) {
            gl.glDeleteBuffers(1, new int[] { bufferHandle }, 0);
        }
        bufferInitialized = false;
        resetBuffer = false;
    }

    public void allocate(int size) {
        if (buffer == null || size < buffer.capacity()) {
            buffer = Buffers.newDirectFloatBuffer(size);
        }

        buffer.clear();
        resetBuffer = true;
    }

    public boolean isRunning() {
        return mark != null;
    }

    public void begin(int mode) {
        mark = new Marker(mode, buffer.position(), 0);
    }

    public void end() {
        if (isRunning()) {
            marks.add(new Marker(mark.mode, mark.start / 2, (buffer.position() - mark.start) / 2));
            mark = null;
        }
    }

    public void add(float x, float y) {
        buffer.put(x).put(y);
    }

    public void draw(GL2 gl) {
        prepareDraw(gl);
        for (Marker mark : marks) {
            gl.glDrawArrays(mark.mode, mark.start, mark.count);
        }
    }

    public void draw(GL2 gl, int modeOverride) {
        prepareDraw(gl);
        for (Marker mark : marks) {
            gl.glDrawArrays(modeOverride, mark.start, mark.count);
        }
    }

    private void prepareDraw(GL2 gl) {
        if (resetBuffer) {
            destroy(gl);
        }

        if (!bufferInitialized) {
            int[] ref = new int[1];
            gl.glGenBuffers(1, ref, 0);

            bufferHandle = ref[0];
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferHandle);

            buffer.flip();
            int numFloats = buffer.limit();
            gl.glBufferData(GL.GL_ARRAY_BUFFER, numFloats * Buffers.SIZEOF_FLOAT, buffer, GL.GL_STATIC_DRAW);

            logGLError(LOGGER, Level.WARNING, gl, "GL Error");

            bufferInitialized = true;
        }

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferHandle);
        gl.glVertexPointer(2, GL.GL_FLOAT, 0, 0);
        gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
    }

    private static class Marker {
        final int mode;
        final int start;
        final int count;

        Marker(int mode, int start, int count) {
            this.mode = mode;
            this.start = start;
            this.count = count;
        }
    }
}

package testplugin.views;

import static com.metsci.glimpse.gl.util.GLErrorUtils.logGLError;

import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLPointerFunc;

import com.jogamp.common.nio.Buffers;

public class SimpleGLBuffer {
    private static final Logger LOGGER = Logger.getLogger(SimpleGLBuffer.class.getName());

    private boolean bufferInitialized;
    private boolean dirty;
    private int[] bufferHandle;

    private FloatBuffer buffer;

    private int numPoints;

    public FloatBuffer getBuffer(int capacity) {
        if (buffer == null || buffer.capacity() < capacity) {
            buffer = Buffers.newDirectFloatBuffer(capacity);
        }

        buffer.clear();
        return buffer;
    }

    public void setDirty() {
        dirty = true;
    }

    public void prepareDraw(GL2 gl) {
        if (!bufferInitialized) {
            bufferHandle = new int[1];
            gl.glGenBuffers(1, bufferHandle, 0);
            bufferInitialized = true;
        }

        if (dirty) {
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferHandle[0]);

            int numFloats = buffer.limit();
            numPoints = numFloats / 2;
            gl.glBufferData(GL.GL_ARRAY_BUFFER, numFloats * Buffers.SIZEOF_FLOAT, buffer.rewind(), GL.GL_STATIC_DRAW);

            logGLError(LOGGER, Level.WARNING, gl, "GL Error");

            dirty = false;
        }
    }

    public void draw(GL2 gl, int mode) {
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferHandle[0]);
        gl.glVertexPointer(2, GL.GL_FLOAT, 0, 0);
        gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);

        gl.glDrawArrays(mode, 0, numPoints);
    }
}

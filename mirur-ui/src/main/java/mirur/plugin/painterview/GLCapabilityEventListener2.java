package mirur.plugin.painterview;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.media.opengl.GLAutoDrawable;

import com.metsci.glimpse.gl.GLCapabilityEventListener;

/**
 * Eclipse's logging seems very slow and logging all OpenGL capabilities at start really makes it seem like the plugin
 * is slow. So here we capture all the capabilities strings and then log in one shot.
 */
public class GLCapabilityEventListener2 extends GLCapabilityEventListener {
    private static final Logger LOGGER = Logger.getLogger(GLCapabilityEventListener2.class.getName());

    private StringBuilder appender;

    public GLCapabilityEventListener2() {
        this(new StringBuilder());
    }

    private GLCapabilityEventListener2(final StringBuilder appender) {
        super(new Logger(".", null) {
            @Override
            public void log(LogRecord record) {
                appender.append("\n");
                appender.append(record.getMessage());
            }
        });
        this.appender = appender;

        appender.append("OpenGL Capabilities List");
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);

        LOGGER.log(Level.INFO, appender.toString());
    }
}
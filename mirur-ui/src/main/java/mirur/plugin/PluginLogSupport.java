package mirur.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.metsci.glimpse.util.logging.format.Formatter;
import com.metsci.glimpse.util.logging.format.TimestampingMethodNameLogFormatter;

public class PluginLogSupport {
    private static final Level LEVEL = Level.ALL;

    public static final void initializeLogger() {
        try (InputStream stream = PluginLogSupport.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException ex) {
            Activator.getDefault().getLog().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Could not initialize logging", ex));
            return;
        }

        Logger logger = Logger.getLogger("");

        if (logger == null) {
            return;
        }

        Handler handler = new Handler() {
            final Formatter formatter = new TimestampingMethodNameLogFormatter();
            final ILog rcpLogger = Activator.getDefault().getLog();

            @Override
            public void publish(LogRecord record) {
                String message = formatter.format(record);
                Level level = record.getLevel();
                Throwable ex = record.getThrown();
                int status = IStatus.INFO;

                if (level == null) {
                    status = IStatus.INFO;
                } else if (level.intValue() >= Level.SEVERE.intValue()) {
                    status = IStatus.ERROR;
                } else if (level.intValue() >= Level.WARNING.intValue()) {
                    status = IStatus.WARNING;
                } else {
                    status = IStatus.INFO;
                }

                rcpLogger.log(new Status(status, Activator.PLUGIN_ID, message, ex));
            }

            @Override
            public void flush() {
                // nop
            }

            @Override
            public void close() throws SecurityException {
                // nop
            }
        };

        handler.setLevel(LEVEL);
        logger.addHandler(handler);
    }

    public static void error(Class<?> src, String message, Throwable throwable) {
        Logger.getLogger(src.getName()).log(Level.WARNING, message, throwable);
    }
}

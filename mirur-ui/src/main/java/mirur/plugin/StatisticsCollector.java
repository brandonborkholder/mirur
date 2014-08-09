package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logInfo;

import java.util.Date;
import java.util.logging.Logger;

import mirur.core.PrimitiveArray;

public class StatisticsCollector {
    private static final Logger LOGGER = Logger.getLogger(StatisticsCollector.class.getName());

    public void started() {
        logInfo(LOGGER, "Starting plugin %s", new Date());
    }

    public void transformedViaAgent(String originalObjectSignature) {
        logInfo(LOGGER, "MirurAgent transformed %s", originalObjectSignature);
    }

    public void receivedFromTarget(PrimitiveArray array) {
        logInfo(LOGGER, "Received %s", array);
    }

    public void selected(PrimitiveArray array) {
        if (array != null) {
            logInfo(LOGGER, "Selected %s", array);
        }
    }
}

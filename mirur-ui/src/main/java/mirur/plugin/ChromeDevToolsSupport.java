package mirur.plugin;

import org.eclipse.debug.core.model.IVariable;

public class ChromeDevToolsSupport {
    private static final String DEBUG_MODEL_ID = "org.chromium.debug";

    private static final boolean isPluginSupported;

    static {
        boolean success;
        try {
            Class.forName("org.chromium.debug.core.model.Value");
            success = true;
        } catch (Exception ex) {
            success = false;
        }

        isPluginSupported = success;
    }

    public static boolean supports(IVariable var) {
        return var.getModelIdentifier().equals(DEBUG_MODEL_ID) && isPluginSupported;
    }
}

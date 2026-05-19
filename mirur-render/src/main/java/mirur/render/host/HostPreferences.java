package mirur.render.host;

/**
 * Key/value preference access abstraction for host IDE integration.
 */
public interface HostPreferences {
    boolean getBoolean(String key, boolean defaultValue);

    int getInt(String key, int defaultValue);

    double getDouble(String key, double defaultValue);

    String getString(String key, String defaultValue);
}

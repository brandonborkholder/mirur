package mirur.render.host;

/**
 * IDE-neutral action abstraction used by shared rendering/view logic.
 */
public interface HostAction {
    String id();

    String label();

    void run();
}

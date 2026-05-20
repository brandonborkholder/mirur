package mirur.render.host;

import java.util.List;

/**
 * IDE-neutral menu abstraction used to supply contextual actions.
 */
public interface HostMenu {
    List<HostAction> actions();
}

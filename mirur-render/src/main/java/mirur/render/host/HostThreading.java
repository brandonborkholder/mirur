package mirur.render.host;

/**
 * Threading abstraction to avoid direct dependency on Eclipse Jobs/UI utilities.
 */
public interface HostThreading {
    void runOnUiThread(Runnable task);

    void runInBackground(String name, Runnable task);
}

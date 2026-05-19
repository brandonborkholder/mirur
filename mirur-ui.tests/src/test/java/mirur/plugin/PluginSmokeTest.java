package mirur.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.junit.Test;
import org.osgi.framework.Bundle;

public class PluginSmokeTest {
    private static final String PLUGIN_ID = "mirur.mirur-ui";
    private static final String STATISTICS_VIEW_ID = "mirur.views.Statistics";
    private static final String STATISTICS_VIEW_CLASS = "mirur.plugin.statsview.ArrayStatsView";
    private static final String PREFERENCES_PAGE_ID = "mirur.preferences.main";

    @Test
    public void startsMirurBundleInsideWorkbenchRuntime() throws Exception {
        Bundle bundle = Platform.getBundle(PLUGIN_ID);

        assertNotNull("Mirur UI bundle should be installed in the test runtime", bundle);
        bundle.start(Bundle.START_TRANSIENT);

        assertEquals("Mirur UI bundle should start successfully", Bundle.ACTIVE, bundle.getState());
        assertTrue("Tycho should launch the Eclipse workbench for these smoke tests", PlatformUI.isWorkbenchRunning());
    }

    @Test
    public void registersExpectedWorkbenchExtensions() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();

        assertExtensionRegistered(registry, "org.eclipse.ui.views", "id", "mirur.views.Painter");
        assertExtensionRegistered(registry, "org.eclipse.ui.views", "id", STATISTICS_VIEW_ID);
        assertExtensionRegistered(registry, "org.eclipse.ui.preferencePages", "id", PREFERENCES_PAGE_ID);
        assertExtensionRegistered(registry, "org.eclipse.debug.ui.detailPaneFactories", "id", "mirur.plugin.detailpane.ArrayDetailPaneFactory");
    }

    @Test
    public void opensStatisticsViewInWorkbench() throws Exception {
        IWorkbenchWindow window = activeWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();

        assertNotNull("Workbench window should have an active page", page);

        IViewPart view = page.showView(STATISTICS_VIEW_ID);
        try {
            assertTrue("Statistics view should instantiate from the registered extension", STATISTICS_VIEW_CLASS.equals(view.getClass().getName()));
        } finally {
            page.hideView(view);
        }
    }

    private static IWorkbenchWindow activeWorkbenchWindow() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window != null) {
            return window;
        }

        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        assertTrue("UI harness should open at least one workbench window", windows.length > 0);
        return windows[0];
    }

    private static void assertExtensionRegistered(IExtensionRegistry registry, String extensionPointId, String attributeName, String attributeValue) {
        for (IConfigurationElement element : registry.getConfigurationElementsFor(extensionPointId)) {
            if (PLUGIN_ID.equals(element.getContributor().getName()) && attributeValue.equals(element.getAttribute(attributeName))) {
                return;
            }
        }

        throw new AssertionError("Expected extension " + extensionPointId + " with " + attributeName + "=" + attributeValue);
    }
}

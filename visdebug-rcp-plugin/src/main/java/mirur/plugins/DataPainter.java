package mirur.plugins;

import org.eclipse.swt.widgets.Menu;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public interface DataPainter {
    GlimpseLayout getLayout();

    void populateMenu(Menu parent);

    void resetAxes();

    void uninstall(GlimpseCanvas canvas);
}

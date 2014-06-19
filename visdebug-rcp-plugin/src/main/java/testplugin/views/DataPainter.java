package testplugin.views;

import org.eclipse.swt.widgets.Menu;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public interface DataPainter {
    GlimpseLayout getLayout();

    Menu populateMenu(Menu parent);

    void resetAxes();

    void uninstall(GlimpseCanvas canvas);
}

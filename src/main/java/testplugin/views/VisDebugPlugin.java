package testplugin.views;

import org.eclipse.swt.graphics.Image;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public interface VisDebugPlugin {
    boolean supportsData(Class<?> clazz);

    String getName();

    Image getIcon();

    void installLayout(GlimpseCanvas canvas, Array1D data);

    void removeLayout(GlimpseCanvas canvas);
}

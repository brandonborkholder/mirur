package testplugin.views;

import org.eclipse.swt.graphics.Image;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public interface VisDebugPlugin {
    boolean supportsData(PrimitiveArray array);

    String getName();

    Image getIcon();

    void installLayout(GlimpseCanvas canvas, PrimitiveArray array);

    void removeLayout(GlimpseCanvas canvas);
}

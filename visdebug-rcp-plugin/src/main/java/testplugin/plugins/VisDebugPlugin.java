package testplugin.plugins;

import org.eclipse.swt.graphics.Image;

import testplugin.views.DataPainter;
import testplugin.views.PrimitiveArray;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public interface VisDebugPlugin {
    boolean supportsData(PrimitiveArray array);

    String getName();

    Image getIcon();

    DataPainter install(GlimpseCanvas canvas, PrimitiveArray array);
}

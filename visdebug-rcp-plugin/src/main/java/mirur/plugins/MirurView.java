package mirur.plugins;

import mirur.core.PrimitiveArray;

import org.eclipse.swt.graphics.Image;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public interface MirurView {
    boolean supportsData(PrimitiveArray array);

    String getName();

    Image getIcon();

    DataPainter install(GlimpseCanvas canvas, PrimitiveArray array);
}

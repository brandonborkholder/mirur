package mirur.plugins;

import mirur.core.PrimitiveArray;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public interface MirurView {
    boolean supportsData(PrimitiveArray array);

    String getName();

    ImageDescriptor getIcon();

    DataPainter install(GlimpseCanvas canvas, PrimitiveArray array);
}

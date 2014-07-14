package mirur.plugins;

import mirur.core.PrimitiveArray;

import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public class InvalidPlaceholderView implements MirurView {
    @Override
    public boolean supportsData(PrimitiveArray array) {
        return true;
    }

    @Override
    public String getName() {
        return "Default show nothing";
    }

    @Override
    public ImageDescriptor getIcon() {
        return null;
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, PrimitiveArray array) {
        InvalidPlaceholderLayout layout = new InvalidPlaceholderLayout();
        canvas.addLayout(layout);

        return new DataPainterImpl(layout);
    }
}

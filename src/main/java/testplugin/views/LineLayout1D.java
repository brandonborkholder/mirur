package testplugin.views;

import org.eclipse.swt.graphics.Image;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public class LineLayout1D implements VisDebugPlugin {
    @Override
    public boolean supportsData(Class<?> clazz) {
        return int[].class.equals(clazz) ||
                float[].class.equals(clazz) ||
                double[].class.equals(clazz) ||
                char[].class.equals(clazz) ||
                short[].class.equals(clazz);
    }

    @Override
    public String getName() {
        return "Line";
    }

    @Override
    public Image getIcon() {
        return null;
    }

    @Override
    public void installLayout(GlimpseCanvas canvas, Array1D data) {
        GlimpseLayout layout = new LinePlot(data);
        canvas.addLayout(layout);
    }

    @Override
    public void removeLayout(GlimpseCanvas canvas) {
        canvas.removeAllLayouts();
    }
}

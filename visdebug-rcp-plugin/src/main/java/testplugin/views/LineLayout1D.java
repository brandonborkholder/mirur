package testplugin.views;

import org.eclipse.swt.graphics.Image;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

public class LineLayout1D implements VisDebugPlugin {
    @Override
    public boolean supportsData(PrimitiveArray array) {
        Class<?> clazz = array.getData().getClass();
        return array instanceof Array1D &&
               (int[].class.equals(clazz) ||
                float[].class.equals(clazz) ||
                double[].class.equals(clazz) ||
                char[].class.equals(clazz) ||
                short[].class.equals(clazz));
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
    public void installLayout(GlimpseCanvas canvas, PrimitiveArray array) {
        GlimpseLayout layout = new LinePlot((Array1D) array);
        canvas.addLayout(layout);
    }

    @Override
    public void removeLayout(GlimpseCanvas canvas) {
        canvas.removeAllLayouts();
    }
}

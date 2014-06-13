package testplugin.plugins;

import org.eclipse.swt.graphics.Image;

import testplugin.views.Array2D;
import testplugin.views.PrimitiveArray;

import com.metsci.glimpse.canvas.GlimpseCanvas;

public abstract class SimplePlugin2D implements VisDebugPlugin {
    private final String name;
    private final Image icon;

    public SimplePlugin2D(String name, Image icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public boolean supportsData(PrimitiveArray array) {
        Class<?> clazz = array.getData().getClass();
        return array instanceof Array2D &&
               ((Array2D) array).isSquare() &&
                (int[][].class.equals(clazz) ||
                 float[][].class.equals(clazz) ||
                 double[][].class.equals(clazz) ||
                 char[][].class.equals(clazz) ||
                 short[][].class.equals(clazz) ||
                 boolean[][].class.equals(clazz));

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Image getIcon() {
        return icon;
    }

    @Override
    public void installLayout(GlimpseCanvas canvas, PrimitiveArray array) {
        installLayout(canvas, (Array2D) array);
    }

    protected abstract void installLayout(GlimpseCanvas canvas, Array2D array);

    @Override
    public void removeLayout(GlimpseCanvas canvas) {
        canvas.removeAllLayouts();
    }
}

package mirur.plugins;

import mirur.core.Array2D;
import mirur.core.PrimitiveArray;

import org.eclipse.swt.graphics.Image;

public abstract class SimplePlugin2D implements MirurView {
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
}

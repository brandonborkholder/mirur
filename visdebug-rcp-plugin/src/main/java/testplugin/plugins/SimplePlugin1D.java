package testplugin.plugins;

import org.eclipse.swt.graphics.Image;

import testplugin.views.Array1D;
import testplugin.views.Array1DPlot;
import testplugin.views.DataPainter;
import testplugin.views.PrimitiveArray;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;

public abstract class SimplePlugin1D implements VisDebugPlugin {
    private final String name;
    private final Image icon;

    public SimplePlugin1D(String name, Image icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public boolean supportsData(PrimitiveArray array) {
        Class<?> clazz = array.getData().getClass();
        return array instanceof Array1D &&
                (int[].class.equals(clazz) ||
                        float[].class.equals(clazz) ||
                        double[].class.equals(clazz) ||
                        char[].class.equals(clazz) ||
                        short[].class.equals(clazz) ||
                        boolean[].class.equals(clazz));

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
    public DataPainter install(GlimpseCanvas canvas, PrimitiveArray array) {
        final Array1D array1d = (Array1D) array;
        GlimpseDataPainter2D painter = createPainter(array1d);
        Array1DPlot plot = new Array1DPlot(painter, array1d);

        canvas.addLayout(plot);
        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAxis(plot.getAxis());
        return result;
    }

    protected abstract GlimpseDataPainter2D createPainter(Array1D array);
}

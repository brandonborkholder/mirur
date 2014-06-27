package mirur.plugins;

import mirur.plugin.Array1D;
import mirur.plugin.PrimitiveArray;

import org.eclipse.swt.graphics.Image;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;

public abstract class SimplePlugin1D implements MirurView {
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
        final Array1DPlot plot = new Array1DPlot(painter, array1d);

        canvas.addLayout(plot);
        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAction(new FisheyeAction() {
            @Override
            public void run() {
                if (isChecked()) {
                    plot.setShaders(new FisheyePipeline());
                } else {
                    plot.setShaders(InitializablePipeline.DEFAULT);
                }
            }
        });
        result.addAxis(plot.getAxis());
        return result;
    }

    protected abstract GlimpseDataPainter2D createPainter(Array1D array);
}

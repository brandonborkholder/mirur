package mirur.plugins;

import mirur.core.Array1D;
import mirur.core.PrimitiveArray;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;

public abstract class SimplePlugin1D implements MirurView {
    private final String name;
    private final ImageDescriptor icon;

    public SimplePlugin1D(String name, ImageDescriptor icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public boolean supportsData(PrimitiveArray array) {
        Class<?> clazz = array.getData().getClass();
        return array instanceof Array1D &&
                (int[].class.equals(clazz) ||
                        long[].class.equals(clazz) ||
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
    public ImageDescriptor getIcon() {
        return icon;
    }

    @Override
    public DataPainter install(GlimpseCanvas canvas, PrimitiveArray array) {
        Array1D array1d = (Array1D) array;
        GlimpseDataPainter2D painter = createPainter(array1d);
        Array1DPlot plot = new Array1DPlot(painter, array1d);

        DataPainterImpl result = new DataPainterImpl(plot);
        result.addAction(getFisheyeAction(plot));
        result.addAxis(plot.getAxis());

        finalInstall(canvas, result);
        return result;
    }

    protected void finalInstall(GlimpseCanvas canvas, DataPainter painter) {
        canvas.addLayout(painter.getLayout());
    }

    protected Action getFisheyeAction(final Array1DPlot plot) {
        return new FisheyeAction() {
            @Override
            public void run() {
                if (isChecked()) {
                    plot.setShaders(new FisheyePipeline());
                } else {
                    plot.setShaders(InitializablePipeline.DEFAULT);
                }
            }
        };
    }

    protected abstract GlimpseDataPainter2D createPainter(Array1D array);
}

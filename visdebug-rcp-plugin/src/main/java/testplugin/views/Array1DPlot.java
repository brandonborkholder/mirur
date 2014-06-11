package testplugin.views;

import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.plot.SimplePlot2D;

public class Array1DPlot extends SimplePlot2D {
    public Array1DPlot(GlimpseDataPainter2D dataPainter, Array1D array) {
        setAxisSizeX(25);
        setAxisSizeY(30);
        setTitle(array.getName());

        addPainter(dataPainter, DATA_LAYER);

        getCrosshairPainter().showSelectionBox(false);

        ArrayValueTipPainter valuePainter = new ArrayValueTipPainter(getAxis(), array);
        addPainter0(valuePainter, null, Integer.MAX_VALUE);

        display(array);
    }

    private void display(Array1D array) {
        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;
        float[] data = array.toFloats();
        for (float v : data) {
            min = Math.min(v, min);
            max = Math.max(v, max);
        }

        getAxisX().setMin(0);
        getAxisX().setMax(data.length);
        getAxisY().setMin(min);
        getAxisY().setMax(max);
    }
}

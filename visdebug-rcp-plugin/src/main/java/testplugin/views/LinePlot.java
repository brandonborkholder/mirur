package testplugin.views;

import com.metsci.glimpse.painter.plot.XYLinePainter;
import com.metsci.glimpse.plot.SimplePlot2D;
import com.metsci.glimpse.support.color.GlimpseColor;

public class LinePlot extends SimplePlot2D {
    private XYLinePainter painter;

    public LinePlot(Array1D array) {
        setTitleHeight(0);
        setAxisSizeX(25);
        setAxisSizeY(30);

        painter = new XYLinePainter();
        addPainter(painter, DATA_LAYER);

        painter.showLines(true);
        painter.showPoints(true);
        painter.setLineColor(GlimpseColor.getRed());
        painter.setPointSize(3);
        painter.setLineThickness(1.5f);

        getCrosshairPainter().showSelectionBox(false);

        ArrayValueTipPainter valuePainter = new ArrayValueTipPainter(getAxis(), array);
        addPainter0(valuePainter, null, Integer.MAX_VALUE);

        display(array);
    }

    private void display(Array1D array) {
        float[] data = array.toFloats();
        float[] indexes = new float[data.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;
        for (float v : data) {
            min = Math.min(v, min);
            max = Math.max(v, max);
        }

        getAxisX().setMin(0);
        getAxisX().setMax(data.length - 1);
        getAxisY().setMin(min);
        getAxisY().setMax(max);

        painter.setData(indexes, data);
    }
}

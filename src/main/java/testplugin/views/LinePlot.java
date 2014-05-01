package testplugin.views;

import com.metsci.glimpse.painter.plot.XYLinePainter;
import com.metsci.glimpse.plot.SimplePlot2D;
import com.metsci.glimpse.support.color.GlimpseColor;

public class LinePlot extends SimplePlot2D {
    private XYLinePainter painter;

    public LinePlot() {
        setTitleHeight(0);
        setAxisSizeX(20);
        setAxisSizeY(20);

        painter = new XYLinePainter();
        addPainter(painter, DATA_LAYER);

        painter.showLines(true);
        painter.showPoints(true);
        painter.setLineColor(GlimpseColor.getRed());
        painter.setPointSize(3);
        painter.setLineThickness(1.5f);
    }

    public void display(Array1D array) {
        float[] data = array.toFloats();
        float[] indexes = new float[data.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        painter.setData(indexes, data);
    }
}

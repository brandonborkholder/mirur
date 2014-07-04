package mirur.plugins;

import mirur.core.Array1D;

import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.painter.info.SimpleTextPainter.VerticalPosition;
import com.metsci.glimpse.plot.SimplePlot2D;
import com.metsci.glimpse.support.color.GlimpseColor;

public class Array1DPlot extends SimplePlot2D {
    private ShaderWrapperPainter shaderWrapper;

    public Array1DPlot(GlimpseDataPainter2D dataPainter, Array1D array) {
        setAxisSizeX(25);
        setTitleHeight(30);

        setData(array);

        shaderWrapper = new ShaderWrapperPainter();
        addPainter(dataPainter, shaderWrapper, DATA_LAYER);
    }

    @Override
    protected void initializePainters() {
        super.initializePainters();

        getCrosshairPainter().showSelectionBox(false);
        titlePainter.setHorizontalPosition(HorizontalPosition.Left);
    }

    @Override
    protected SimpleTextPainter createTitlePainter() {
        SimpleTextPainter painter = new Array1DTitlePainter(getAxis());
        painter.setHorizontalPosition(HorizontalPosition.Left);
        painter.setVerticalPosition(VerticalPosition.Center);
        painter.setColor(GlimpseColor.getBlack());
        return painter;
    }

    public void setShaders(InitializablePipeline pipeline) {
        shaderWrapper.setPipeline(pipeline);
    }

    private void setData(Array1D array) {
        ((Array1DTitlePainter) titlePainter).setArray(array);

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

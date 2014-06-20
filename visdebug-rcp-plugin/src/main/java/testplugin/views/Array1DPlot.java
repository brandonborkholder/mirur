package testplugin.views;

import testplugin.ShaderWrapperPainter;
import testplugin.plugins.FisheyePipeline;

import com.metsci.glimpse.axis.painter.NumericAxisPainter;
import com.metsci.glimpse.axis.painter.label.AxisLabelHandler;
import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.painter.base.GlimpseDataPainter2D;
import com.metsci.glimpse.painter.base.GlimpsePainter;
import com.metsci.glimpse.plot.SimplePlot2D;

public class Array1DPlot extends SimplePlot2D {
    private ShaderWrapperPainter shaderWrapper;

    public Array1DPlot(GlimpseDataPainter2D dataPainter, Array1D array) {
        setAxisSizeX(25);
        setAxisSizeY(30);
        setTitle(array.getSignature() + " " + array.getName());

        getCrosshairPainter().showSelectionBox(false);

        ArrayValueTipPainter valuePainter = new ArrayValueTipPainter(getAxis(), array);
        addPainter0(valuePainter, null, Integer.MAX_VALUE);

        display(array);

        shaderWrapper = new ShaderWrapperPainter();

        GlimpseLayout layoutX = getLayoutX();
        GlimpsePainter p = getAxisPainterX();
        layoutX.removePainter(p);
        layoutX.addPainter(p, shaderWrapper);

        GlimpseLayout layoutXY = getLayoutCenter();
        p = dataPainter;
        layoutXY.addPainter(p, shaderWrapper, DATA_LAYER);
        layoutXY.removePainter(getGridPainter());
        layoutXY.addPainter(getGridPainter(), shaderWrapper, BACKGROUND_LAYER);
        layoutXY.removePainter(getCrosshairPainter());
        layoutXY.addPainter(getCrosshairPainter(), shaderWrapper, FOREGROUND_LAYER);

        shaderWrapper.setPipeline(new FisheyePipeline());
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

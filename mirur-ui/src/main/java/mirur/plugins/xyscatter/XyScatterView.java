package mirur.plugins.xyscatter;

import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.painter.info.SimpleTextPainter;
import com.metsci.glimpse.painter.info.SimpleTextPainter.HorizontalPosition;
import com.metsci.glimpse.plot.Plot2D;
import com.metsci.glimpse.plot.SimplePlot2D;

import mirur.core.Array1D;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.AxisUtils;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.SimplePlugin2D;

public class XyScatterView extends SimplePlugin2D {
    public XyScatterView() {
        super("xy Scatter", null);
    }

    @Override
    public boolean supportsData(VariableObject obj) {
        if (obj instanceof Array1D) {
            Class<?> clazz = ((Array1D) obj).getData().getClass();
            return int[].class.equals(clazz) ||
                    long[].class.equals(clazz) ||
                    float[].class.equals(clazz) ||
                    double[].class.equals(clazz) ||
                    char[].class.equals(clazz) ||
                    short[].class.equals(clazz);
        } else {
            return false;
        }
    }

    @Override
    public DataPainter create(GlimpseCanvas canvas, VariableObject obj) {
        Array1D array = (Array1D) obj;

        DataUnitConverter xUnitConverter = DataUnitConverter.IDENTITY;
        DataUnitConverter yUnitConverter = DataUnitConverter.IDENTITY;
        XyPointIndex index = new XyPointIndex(xUnitConverter, yUnitConverter);
        VisitArray.visit1d(array.getData(), index);

        SimplePlot2D plot = new SimplePlot2D() {
            @Override
            protected SimpleTextPainter createTitlePainter() {
                SimpleTextPainter p = super.createTitlePainter();
                p.setHorizontalPosition(HorizontalPosition.Left);
                return p;
            }
        };
        plot.setTitleHeight(30);
        plot.setTitle(String.format("%s %s[%d]", array.getSignature(), array.getName(), array.getSize()));

        XyArrayTooltipPainter tooltipPainter = new XyArrayTooltipPainter(plot.getAxis(), index, array);
        plot.getLayoutCenter().addPainter(tooltipPainter, Plot2D.FOREGROUND_LAYER);

        XyPointsPainter pointsPainter = new XyPointsPainter();
        pointsPainter.setData(array, xUnitConverter, yUnitConverter);
        plot.getLayoutCenter().addPainter(pointsPainter, Plot2D.DATA_LAYER);

        plot.getAxisX().addAxisListener(new FixedPixelSelectionSizeListener(10));
        plot.getAxisY().addAxisListener(new FixedPixelSelectionSizeListener(10));

        VisitArray.visit1d(array.getData(), new UpdateXyAxesVisitor()).updateAxes(plot.getAxis());
        AxisUtils.padAxis2d(plot.getAxis());
        plot.getAxis().validate();

        DataPainterImpl painter = new DataPainterImpl(plot);
        painter.addAxis(plot.getAxis());
        painter.addAction(new ShowAsDensityAction(pointsPainter));

        return painter;
    }
}

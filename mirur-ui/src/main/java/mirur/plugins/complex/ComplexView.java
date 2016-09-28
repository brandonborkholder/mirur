package mirur.plugins.complex;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.plot.stacked.PlotInfo;

import mirur.core.Array1D;
import mirur.core.Array1DImpl;
import mirur.core.Array2D;
import mirur.core.VariableObject;
import mirur.core.VisitArray;
import mirur.plugins.Array1DPlot;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.DataUnitConverter;
import mirur.plugins.SimplePlugin2D;
import mirur.plugins.line1d.LinePainter;

public class ComplexView extends SimplePlugin2D {
    public ComplexView() {
        super("Complex (Interleaved)", null);
    }

    @Override
    public boolean supportsData(VariableObject obj) {
        if (obj instanceof Array2D) {
            Class<?> clazz = ((Array2D) obj).getData().getClass();
            return false;
            // return int[][].class.equals(clazz) ||
            // long[][].class.equals(clazz) ||
            // float[][].class.equals(clazz) ||
            // double[][].class.equals(clazz) ||
            // char[][].class.equals(clazz) ||
            // short[][].class.equals(clazz);
        } else if (obj instanceof Array1D) {
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
    public DataPainter install(GlimpseCanvas canvas, VariableObject obj) {
        if (obj instanceof Array1D) {
            Array1D angles = toAngles((Array1D) obj);
            Array1D magnitudes = toMagnitudes((Array1D) obj);
            ComplexPlotLayout layout = new ComplexPlotLayout();
            Axis1D commonAxis = layout.getCommonAxis();

            PlotInfo angPlot = layout.createPlot();
            createAnglePainter(angles, angPlot);
            PlotInfo magPlot = layout.createPlot();
            createMagnitudePainter(magnitudes, magPlot);

            DataPainterImpl result = new DataPainterImpl(layout);
            result.addAxis(angPlot.getOrthogonalAxis());
            result.addAxis(magPlot.getOrthogonalAxis());
            result.addAxis(commonAxis);
            canvas.addLayout(layout);
            return result;
        } else {
            return null;
        }
    }

    protected Array1D toMagnitudes(Array1D obj) {
        Object newData = VisitArray.visit1d(obj.getData(), new CplxMagnitudeVisitor()).get();
        return new Array1DImpl(obj.getName(), newData);
    }

    protected Array1D toAngles(Array1D obj) {
        Object newData = VisitArray.visit1d(obj.getData(), new CplxAngleVisitor()).get();
        return new Array1DImpl(obj.getName(), newData);
    }

    protected void createAnglePainter(Array1D array, final PlotInfo plot) {
        DataUnitConverter dataConverter = DataUnitConverter.IDENTITY;
        LinePainter painter = new LinePainter(array, dataConverter);

        Array1DPlot arrayPlot = new Array1DPlot(painter, array, dataConverter) {
            @Override
            protected Axis1D createAxisX() {
                return plot.getCommonAxis();
            }

            @Override
            protected Axis1D createAxisY() {
                return plot.getOrthogonalAxis();
            }
        };
        plot.getBaseLayout().removeAllLayouts();
        plot.getBaseLayout().addLayout(arrayPlot);
    }

    private void createMagnitudePainter(Array1D array, PlotInfo plot) {
        createAnglePainter(array, plot);
    }
}

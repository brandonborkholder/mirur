package mirur.plugins.complex;

import com.metsci.glimpse.axis.Axis1D;
import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.layout.GlimpseLayout;

import mirur.core.Array1D;
import mirur.core.Array2D;
import mirur.core.VariableObject;
import mirur.plugins.DataPainter;
import mirur.plugins.DataPainterImpl;
import mirur.plugins.SimplePlugin2D;

public class ComplexView extends SimplePlugin2D {
    public ComplexView() {
        super("Complex (Interleaved)", null);
    }

    @Override
    public boolean supportsData(VariableObject obj) {
        if (obj instanceof Array2D) {
            Class<?> clazz = ((Array2D) obj).getData().getClass();
            return int[][].class.equals(clazz) ||
                    long[][].class.equals(clazz) ||
                    float[][].class.equals(clazz) ||
                    double[][].class.equals(clazz) ||
                    char[][].class.equals(clazz) ||
                    short[][].class.equals(clazz);
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
            GlimpseLayout layout = new GlimpseLayout();

            Axis1D parentAxis = new Axis1D();

            GlimpseLayout magLayout = createMagnitudePainter((Array1D) obj, parentAxis);
            GlimpseLayout angLayout = createAnglePainter((Array1D) obj, parentAxis);
            magLayout.setLayoutData("cell 0 0 1 1, grow, push");
            angLayout.setLayoutData("cell 1 0 1 1, grow, push");
            layout.addLayout(magLayout);
            layout.addLayout(angLayout);

            DataPainterImpl painter = new DataPainterImpl(layout);
            return painter;
        } else {
            return null;
        }
    }

    protected GlimpseLayout createAnglePainter(Array1D array, Axis1D parentAxis) {

    }

    protected GlimpseLayout createMagnitudePainter(Array1D array, Axis1D parentAxis) {
    }
}

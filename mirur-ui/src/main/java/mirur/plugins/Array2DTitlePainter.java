package mirur.plugins;

import mirur.core.Array2D;
import mirur.core.ElementToStringVisitor;
import mirur.core.VisitArray;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.info.SimpleTextPainter;

public class Array2DTitlePainter extends SimpleTextPainter {
    private final Axis2D srcAxis;
    private Array2D array;
    private int lastI;
    private int lastJ;

    public Array2DTitlePainter(Axis2D srcAxis) {
        this.srcAxis = srcAxis;
    }

    public void setArray(Array2D array) {
        this.array = array;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        if (array == null) {
            return;
        }

        double selectedX = srcAxis.getAxisX().getSelectionCenter();
        double selectedY = srcAxis.getAxisY().getSelectionCenter();
        int i = (int) Math.round(selectedX);
        int j = (int) Math.round(selectedY);
        if (i != lastI || j != lastJ) {
            String text = format(i, j);
            lastI = i;
            lastJ = j;
            setText(text);
        }

        super.paintTo(context);
    }

    private String format(int i, int j) {
        String value = VisitArray.visit(array.getData(), new ElementToStringVisitor(), i, j).getText();
        if (value == null) {
            Class<?> innerComponent = array.getData().getClass().getComponentType().getComponentType();
            return String.format("%s[%d][%s] %s", innerComponent, array.getSize(0), array.getSize(1), array.getName());
        } else {
            return String.format("%s[%d][%d] = %s", array.getName(), i, j, value);
        }
    }
}

package mirur.plugins;

import mirur.core.Array1D;
import mirur.core.ElementToStringVisitor;
import mirur.core.VisitArray;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.info.SimpleTextPainter;

public class Array1DTitlePainter extends SimpleTextPainter {
    private final Axis2D srcAxis;
    private Array1D array;
    private int lastIndex;

    public Array1DTitlePainter(Axis2D srcAxis) {
        this.srcAxis = srcAxis;
    }

    public void setArray(Array1D array) {
        this.array = array;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        if (array == null) {
            return;
        }

        double selected = srcAxis.getAxisX().getSelectionCenter();
        int idx = (int) Math.round(selected);
        if (idx != lastIndex) {
            text = format(idx);
            lastIndex = idx;
            setText(text);
        }

        super.paintTo(context);
    }

    private String format(int index) {
        if (0 <= index && index < array.getSize(0)) {
            String value = VisitArray.visit(array.getData(), new ElementToStringVisitor(), index).getText();
            return String.format("%s[%d] = %s", array.getName(), index, value);
        } else {
            return String.format("%s[%d] %s", array.getData().getClass().getComponentType(), array.getSize(0), array.getName());
        }
    }
}

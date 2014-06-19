package testplugin.views;

import javax.media.opengl.GL2;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.decoration.LegendPainter;
import com.metsci.glimpse.support.color.GlimpseColor;

public class ArrayValueTipPainter extends LegendPainter {
    private final Axis2D srcAxis;
    private final Array1D array;
    private int lastIndex;

    public ArrayValueTipPainter(Axis2D srcAxis, Array1D array) {
        super(LegendPlacement.SE);
        this.srcAxis = srcAxis;
        this.array = array;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        double selected = srcAxis.getAxisX().getSelectionCenter();
        int idx = (int) Math.round(selected);
        if (idx != lastIndex) {
            String text = "N/A";
            if (0 <= idx && idx < array.getSize(0)) {
                text = "[" + idx + "] = " + format(idx);
            }

            clear();
            addItem(text, GlimpseColor.getBlack());
            lastIndex = idx;
        }

        super.paintTo(context);
    }

    private String format(int index) {
        Object data = array.getData();
        if (data instanceof int[]) {
            return String.valueOf(((int[]) data)[index]);
        } else if (data instanceof float[]) {
            return String.valueOf(((float[]) data)[index]);
        } else if (data instanceof double[]) {
            return String.valueOf(((double[]) data)[index]);
        } else if (data instanceof char[]) {
            return String.valueOf((int) ((char[]) data)[index]);
        } else if (data instanceof short[]) {
            return String.valueOf(((short[]) data)[index]);
        } else if (data instanceof boolean[]) {
            return String.valueOf(((boolean[]) data)[index]);
        } else {
            throw new AssertionError();
        }
    }

    @Override
    protected void drawLegendItem(GL2 gl, String label, int xpos, int ypos, float[] rgba, int height) {
        // do nothing
    }
}

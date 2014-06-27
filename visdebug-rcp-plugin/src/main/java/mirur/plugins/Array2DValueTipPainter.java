package mirur.plugins;

import javax.media.opengl.GL2;

import mirur.plugin.Array2D;

import com.metsci.glimpse.axis.Axis2D;
import com.metsci.glimpse.context.GlimpseContext;
import com.metsci.glimpse.painter.decoration.LegendPainter;
import com.metsci.glimpse.support.color.GlimpseColor;

public class Array2DValueTipPainter extends LegendPainter {
    private final Axis2D srcAxis;
    private final Array2D array;
    private int lastI;
    private int lastJ;

    public Array2DValueTipPainter(Axis2D srcAxis, Array2D array) {
        super(LegendPlacement.SE);
        this.srcAxis = srcAxis;
        this.array = array;
    }

    @Override
    public void paintTo(GlimpseContext context) {
        double selectedX = srcAxis.getAxisX().getSelectionCenter();
        double selectedY = srcAxis.getAxisY().getSelectionCenter();
        int i = (int) Math.round(selectedX);
        int j = (int) Math.round(selectedY);
        if (i != lastI || j != lastJ) {
            String text = "[" + i + "][" + j + "] = " + format(i, j);
            clear();
            addItem(text, GlimpseColor.getBlack());
            lastI = i;
            lastJ = j;
        }

        super.paintTo(context);
    }

    private String format(int i, int j) {
        Object data = array.getData();
        if (data instanceof int[][]) {
            return String.valueOf(((int[][]) data)[i][j]);
        } else if (data instanceof float[][]) {
            return String.valueOf(((float[][]) data)[i][j]);
        } else if (data instanceof double[][]) {
            return String.valueOf(((double[][]) data)[i][j]);
        } else if (data instanceof char[][]) {
            return String.valueOf((int) ((char[][]) data)[i][j]);
        } else if (data instanceof short[][]) {
            return String.valueOf(((short[][]) data)[i][j]);
        } else if (data instanceof boolean[][]) {
            return String.valueOf(((boolean[][]) data)[i][j]);
        } else {
            throw new AssertionError();
        }
    }

    @Override
    protected void drawLegendItem(GL2 gl, String label, int xpos, int ypos, float[] rgba, int height) {
        // do nothing
    }
}

package testplugin.views;

import javax.media.opengl.GLProfile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.plot.SimplePlot2D;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;
import com.metsci.glimpse.swt.misc.SwtLookAndFeel;

public class GlimpseArrayView extends ViewPart implements DebugViewListener {
    private NewtSwtGlimpseCanvas canvas;
    private FPSAnimator animator;

    private GlimpseLayout currentLayout;

    @Override
    public void createPartControl(Composite parent) {
        canvas = new NewtSwtGlimpseCanvas(parent, GLProfile.getGL2GL3(), SWT.DOUBLE_BUFFERED);
        canvas.setLookAndFeel(new SwtLookAndFeel());
        animator = new FPSAnimator(canvas.getGLDrawable(), 20);
        animator.start();

        currentLayout = new SimplePlot2D();

        canvas.addLayout(currentLayout);

        DebugViewListeners.SINGLETON.add(this);
    }

    @Override
    public void setFocus() {
        canvas.getCanvas().setFocus();
    }

    @Override
    public void dispose() {
        DebugViewListeners.SINGLETON.remove(this);
        animator.stop();
        super.dispose();
    }

    @Override
    public void inspect(int[] data) {
    }

    @Override
    public void inspect(float[] data) {
    }

    @Override
    public void inspect(short[] data) {
    }

    @Override
    public void inspect(byte[] data) {
    }

    @Override
    public void inspect(double[] data) {
        swapLayout(new LinePlot(), new Array1DFloats(data));
    }

    @Override
    public void inspect(long[] data) {
    }

    @Override
    public void inspect(char[] data) {
    }

    @Override
    public void clear() {
    }

    private void swapLayout(GlimpseLayout newLayout, Array1D array) {
        canvas.removeLayout(currentLayout);
        currentLayout = newLayout;
        canvas.addLayout(newLayout);
        ((LinePlot) newLayout).display(array);
    }
}

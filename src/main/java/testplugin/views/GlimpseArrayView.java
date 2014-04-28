package testplugin.views;

import javax.media.opengl.GLProfile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.painter.base.GlimpsePainter;
import com.metsci.glimpse.painter.info.FpsPainter;
import com.metsci.glimpse.plot.Plot2D;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;

public class GlimpseArrayView extends ViewPart {
    private NewtSwtGlimpseCanvas canvas;
    private FPSAnimator animator;

    @Override
    public void createPartControl(Composite parent) {
        canvas = new NewtSwtGlimpseCanvas(parent, GLProfile.getGL2GL3(), SWT.DOUBLE_BUFFERED);
        animator = new FPSAnimator(canvas.getGLDrawable(), 20);
        animator.start();

        GlimpsePainter painter = new FpsPainter();
        Plot2D plot = new Plot2D("none");
        plot.addPainter(painter);

        canvas.addLayout(plot);
    }

    @Override
    public void setFocus() {
        canvas.getCanvas().setFocus();
    }

    @Override
    public void dispose() {
        animator.stop();
    }
}

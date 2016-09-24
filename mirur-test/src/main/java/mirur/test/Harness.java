package mirur.test;

import javax.media.opengl.GLProfile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.gl.GLCapabilityEventListener;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;

import mirur.plugin.painterview.MirurLAF;
import mirur.plugins.DataPainter;
import mirur.plugins.InvalidPlaceholderView;
import mirur.plugins.MirurView;

public class Harness {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout());

        MirurView view = new InvalidPlaceholderView();

        NewtSwtGlimpseCanvas canvas = new NewtSwtGlimpseCanvas(shell, GLProfile.get(GLProfile.GL2), SWT.DOUBLE_BUFFERED);
        canvas.getGLDrawable().addGLEventListener(new GLCapabilityEventListener());
        FPSAnimator animator = new FPSAnimator(canvas.getGLDrawable(), 20);
        animator.start();

        canvas.setLayoutData(new GridData(GridData.FILL_BOTH));

        DataPainter painter = view.install(canvas, null);
        canvas.setLookAndFeel(new MirurLAF());

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}

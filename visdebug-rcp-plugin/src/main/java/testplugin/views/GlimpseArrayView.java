package testplugin.views;

import static testplugin.views.Model.MODEL;

import javax.media.opengl.GLProfile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import test_plugin.Activator;
import testplugin.plugins.VisDebugPlugin;

import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;
import com.metsci.glimpse.swt.misc.SwtLookAndFeel;

public class GlimpseArrayView extends ViewPart implements ArraySelectListener {
    private NewtSwtGlimpseCanvas canvas;
    private FPSAnimator animator;

    private GlimpseLayout currentLayout;
    private GlimpseLayout invalidPlaceholder;

    private VisDebugPlugin currentPlugin;
    private PrimitiveArray currentData;
    private DataPainter currentPainter;

    @Override
    public void createPartControl(Composite parent) {
        canvas = new NewtSwtGlimpseCanvas(parent, GLProfile.getGL2GL3(), SWT.DOUBLE_BUFFERED);
        canvas.setLookAndFeel(new SwtLookAndFeel());
        animator = new FPSAnimator(canvas.getGLDrawable(), 20);
        animator.start();

        invalidPlaceholder = new InvalidPlaceholderLayout();
        currentLayout = invalidPlaceholder;

        canvas.addLayout(currentLayout);
        canvas.setLookAndFeel(new SwtLookAndFeel());

        getViewSite().getActionBars().getToolBarManager().add(new ListDisplaysAction() {
            @Override
            protected void setPainter(VisDebugPlugin painter) {
                updatePainter(painter);
            }

            @Override
            protected PrimitiveArray getActiveData() {
                return currentData;
            }
        });

        Activator.getDefault().initVariableSelectListener(this);
        MODEL.addArrayListener(this);
    }

    @Override
    public void setFocus() {
        canvas.getCanvas().setFocus();
    }

    @Override
    public void dispose() {
        MODEL.removeArrayListener(this);
        animator.stop();
        super.dispose();
    }

    @Override
    public void selected(PrimitiveArray array) {
        currentData = array;
        refreshDataAndPainter();
    }

    private void updatePainter(VisDebugPlugin plugin) {
        currentPlugin = plugin;
        refreshDataAndPainter();
    }

    private void refreshDataAndPainter() {
        if (currentPainter != null) {
            currentPainter.uninstall(canvas);
        }

        if (currentPlugin != null && currentData != null && currentPlugin.supportsData(currentData)) {
            currentPainter = currentPlugin.install(canvas, currentData);
        } else {
            canvas.addLayout(invalidPlaceholder);
        }
    }
}

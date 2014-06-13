package testplugin.views;

import javax.media.opengl.GLProfile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import testplugin.plugins.VisDebugPlugin;

import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;
import com.metsci.glimpse.swt.misc.SwtLookAndFeel;

public class GlimpseArrayView extends ViewPart {
    private static final String VARIABLE_VIEW_ID = "org.eclipse.debug.ui.VariableView";

    private NewtSwtGlimpseCanvas canvas;
    private FPSAnimator animator;

    private GlimpseLayout currentLayout;
    private GlimpseLayout invalidPlaceholder;

    private VisDebugPlugin currentPainter;
    private PrimitiveArray currentData;

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

        getViewSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(VARIABLE_VIEW_ID, new VariableSelectListener() {
            @Override
            protected void setSelection(PrimitiveArray selected) {
                inspect(selected);
            }
        });
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
    }

    @Override
    public void setFocus() {
        canvas.getCanvas().setFocus();
    }

    @Override
    public void dispose() {
        animator.stop();
        super.dispose();
    }

    private void inspect(PrimitiveArray selected) {
        currentData = selected;
        refreshDataAndPainter();
    }

    private void updatePainter(VisDebugPlugin painter) {
        currentPainter = painter;
        refreshDataAndPainter();
    }

    private void refreshDataAndPainter() {
        canvas.removeAllLayouts();
        if (currentPainter != null && currentData != null && currentPainter.supportsData(currentData)) {
            currentPainter.installLayout(canvas, currentData);
        } else {
            canvas.addLayout(invalidPlaceholder);
        }
    }
}

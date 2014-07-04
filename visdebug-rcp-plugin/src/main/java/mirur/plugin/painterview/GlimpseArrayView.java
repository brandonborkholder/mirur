package mirur.plugin.painterview;

import static mirur.plugin.Model.MODEL;

import javax.media.opengl.GLProfile;

import mirur.core.PrimitiveArray;
import mirur.plugin.ArraySelectListener;
import mirur.plugin.InvalidPlaceholderLayout;
import mirur.plugin.ListDisplaysAction;
import mirur.plugin.PluginMenuAction;
import mirur.plugin.ResetAxesAction;
import mirur.plugins.DataPainter;
import mirur.plugins.MirurView;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.layout.GlimpseLayout;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;
import com.metsci.glimpse.swt.misc.SwtLookAndFeel;

public class GlimpseArrayView extends ViewPart implements ArraySelectListener {
    private ResetAxesAction resetAction;

    private NewtSwtGlimpseCanvas canvas;
    private AnimatorBase animator;

    private GlimpseLayout currentLayout;
    private GlimpseLayout invalidPlaceholder;

    private MirurView currentPlugin;
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

        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        tbm.add(new ListDisplaysAction() {
            @Override
            protected void setPainter(MirurView painter) {
                updatePainter(painter);
            }

            @Override
            protected PrimitiveArray getActiveData() {
                return currentData;
            }
        });
        resetAction = new ResetAxesAction() {
            @Override
            public void run() {
                resetAxes();
            }
        };
        tbm.add(new PluginMenuAction() {
            @Override
            public Menu getMenu(Menu parent) {
                if (currentPainter != null) {
                    currentPainter.populateMenu(parent);
                }

                return parent;
            }
        });
        tbm.add(resetAction);

        MODEL.addArrayListener(this, this);
    }

    @Override
    public void setFocus() {
        canvas.getCanvas().setFocus();
    }

    @Override
    public void dispose() {
        MODEL.removeArrayListener(this, this);
        animator.stop();
        super.dispose();
    }

    @Override
    public void arraySelected(PrimitiveArray array) {
        currentData = array;
        refreshDataAndPainter();
    }

    private void updatePainter(MirurView plugin) {
        currentPlugin = plugin;
        refreshDataAndPainter();
    }

    private void resetAxes() {
        if (currentPainter != null) {
            currentPainter.resetAxes();
        }
    }

    private void refreshDataAndPainter() {
        if (currentPainter != null) {
            resetAction.setEnabled(false);
            currentPainter.uninstall(canvas);
            animator.stop();
        }

        if (currentPlugin != null && currentData != null && currentPlugin.supportsData(currentData)) {
            currentPainter = currentPlugin.install(canvas, currentData);
            resetAction.setEnabled(true);
            animator.start();
        } else {
            canvas.addLayout(invalidPlaceholder);
            canvas.paint();
        }
    }
}

package mirur.plugin.painterview;

import javax.media.opengl.GLProfile;

import mirur.core.PrimitiveArray;
import mirur.plugin.ArraySelectListener;
import mirur.plugin.MirurLAF;
import mirur.plugin.ResetAxesAction;
import mirur.plugin.SelectListenerToggle;
import mirur.plugin.SelectViewAction;
import mirur.plugin.ViewMenuAction;
import mirur.plugins.DataPainter;
import mirur.plugins.InvalidPlaceholderView;
import mirur.plugins.MirurView;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.gl.GLCapabilityEventListener;
import com.metsci.glimpse.support.settings.LookAndFeel;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;

public class GlimpseArrayView extends ViewPart implements ArraySelectListener {
    private static final String ID = "mirur.views.Painter";

    private ResetAxesAction resetAction;
    private ViewMenuAction viewMenuAction;
    private SelectListenerToggle selectListenerToggle;

    private LookAndFeel laf;
    private NewtSwtGlimpseCanvas canvas;
    private AnimatorBase animator;

    private InvalidPlaceholderView invalidPlaceholder;

    private MirurView currentPlugin;
    private PrimitiveArray currentData;
    private DataPainter currentPainter;

    @Override
    public void createPartControl(Composite parent) {
        canvas = new BugFixNewtSwtGlimpseCanvas(parent, GLProfile.getGL2GL3(), SWT.DOUBLE_BUFFERED);
        canvas.getGLDrawable().addGLEventListener(new GLCapabilityEventListener());
        laf = new MirurLAF();
        animator = new FPSAnimator(canvas.getGLDrawable(), 20);
        animator.start();

        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        tbm.add(new SelectViewAction() {
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
        viewMenuAction = new ViewMenuAction() {
            @Override
            public Menu getMenu(Menu parent) {
                if (currentPainter != null) {
                    currentPainter.populateMenu(parent);
                }

                return parent;
            }
        };
        selectListenerToggle = new SelectListenerToggle(ID, this, this);

        tbm.add(viewMenuAction);
        tbm.add(resetAction);
        tbm.add(selectListenerToggle);

        getSite().getPage().addPartListener(selectListenerToggle);

        invalidPlaceholder = new InvalidPlaceholderView();

        refreshDataAndPainter();
    }

    @Override
    public void setFocus() {
        canvas.getCanvas().setFocus();
    }

    @Override
    public void dispose() {
        getSite().getPage().removePartListener(selectListenerToggle);
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
        resetAction.setEnabled(false);
        viewMenuAction.setEnabled(false);
        animator.stop();

        if (currentPainter != null) {
            currentPainter.uninstall(canvas);
        }

        if (currentData != null && currentPlugin != null && currentPlugin.supportsData(currentData)) {
            currentPainter = currentPlugin.install(canvas, currentData);
            resetAction.setEnabled(true);
            viewMenuAction.setEnabled(true);
            canvas.setLookAndFeel(laf);
            animator.start();
        } else {
            currentPainter = invalidPlaceholder.install(canvas, currentData);
            canvas.setLookAndFeel(laf);
            canvas.paint();
        }
    }
}

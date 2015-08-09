/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugin.painterview;

import static mirur.plugin.Activator.getViewSelectionModel;

import javax.media.opengl.GLProfile;

import mirur.core.PrimitiveArray;
import mirur.plugin.ArraySelectListener;
import mirur.plugin.SelectListenerToggle;
import mirur.plugin.ViewSelectListener;
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

public class GlimpseArrayView extends ViewPart implements ArraySelectListener, ViewSelectListener {
    private static final String ID = "mirur.views.Painter";

    private ResetAxesAction resetAction;
    private ViewMenuAction viewMenuAction;
    private SelectListenerToggle selectListenerToggle;

    private LookAndFeel laf;
    private NewtSwtGlimpseCanvas canvas;
    private AnimatorBase animator;

    private InvalidPlaceholderView invalidPlaceholder;

    private MirurView currentView;
    private PrimitiveArray currentData;
    private DataPainter currentPainter;

    @Override
    public void createPartControl(Composite parent) {
        canvas = new NewtSwtGlimpseCanvas(parent, GLProfile.getGL2GL3(), SWT.DOUBLE_BUFFERED);
        canvas.getGLDrawable().addGLEventListener(new GLCapabilityEventListener());
        laf = new MirurLAF();
        animator = new FPSAnimator(canvas.getGLDrawable(), 20);
        animator.start();

        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        tbm.add(new SelectViewAction());
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

        getViewSelectionModel().addArrayListener(this);
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

    @Override
    public void viewSelected(MirurView view) {
        currentView = view;
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

        if (currentData != null && currentView != null && currentView.supportsData(currentData)) {
            currentPainter = currentView.install(canvas, currentData);
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

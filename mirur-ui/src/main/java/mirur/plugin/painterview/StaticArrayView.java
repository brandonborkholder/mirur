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

import javax.media.opengl.GLAnimatorControl;
import javax.media.opengl.GLContext;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.support.settings.LookAndFeel;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;

import mirur.core.VariableObject;
import mirur.plugins.DataPainter;

public class StaticArrayView extends ViewPart {
    static final String ID = "mirur.views.StaticPainter";

    private Composite parent;
    private NewtSwtGlimpseCanvas canvas;
    private GLAnimatorControl animator;

    private DataPainter currentPainter;

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        tbm.add(new ResetAxesAction() {
            @Override
            public void run() {
                currentPainter.resetAxes();
            }
        });
        tbm.add(new ViewMenuAction() {
            @Override
            public Menu getMenu(Menu parent) {
                currentPainter.populateConfigMenu(parent);
                return parent;
            }
        });

        IMenuManager mm = getViewSite().getActionBars().getMenuManager();
        mm.add(new Action("Rename View") {
            @Override
            public void run() {
                setPartName("TODO");
            }
        });
    }

    @Override
    public void setFocus() {
        if (canvas != null) {
            canvas.getCanvas().setFocus();
        }
    }

    @Override
    public void dispose() {
        if (canvas != null) {
            animator.stop();
            currentPainter.detach(canvas);
        }

        super.dispose();
    }

    public void initializePainter(GLContext sharedContext, VariableObject obj, DataPainter painter, LookAndFeel laf) {
        canvas = new NewtSwtGlimpseCanvas(parent, sharedContext, SWT.DOUBLE_BUFFERED);

        setPartName(obj.getName());

        currentPainter = painter;
        currentPainter.attach(canvas);
        canvas.setLookAndFeel(laf);

        animator = new FPSAnimator(canvas.getGLDrawable(), 20);
        animator.start();
    }
}

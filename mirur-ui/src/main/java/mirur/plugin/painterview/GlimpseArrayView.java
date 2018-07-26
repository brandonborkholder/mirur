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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GLAnimatorControl;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.gl.util.GLUtils;
import com.metsci.glimpse.support.settings.LookAndFeel;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;
import com.metsci.glimpse.util.Pair;

import mirur.core.VariableObject;
import mirur.plugin.SelectListenerToggle;
import mirur.plugin.VarObjectSelectListener;
import mirur.plugin.ViewSelectListener;
import mirur.plugin.ViewSelectionModel;
import mirur.plugin.statsview.SaveArrayToFileAction;
import mirur.plugins.DataPainter;
import mirur.plugins.InvalidPlaceholderView;
import mirur.plugins.MirurView;

public class GlimpseArrayView extends ViewPart implements VarObjectSelectListener, ViewSelectListener {
    private static final String ID = "mirur.views.Painter";

    private ResetAxesAction resetAction;
    private SaveArrayToFileAction saveArrayAction;
    private PinPaintersMenuAction pinPaintersAction;
    private ViewMenuAction viewMenuAction;
    private SelectListenerToggle selectListenerToggle;
    private ViewSelectionModel viewSelectModel;

    private LookAndFeel laf;
    private NewtSwtGlimpseCanvas canvas;
    private GLAnimatorControl animator;

    private InvalidPlaceholderView invalidPlaceholder;

    private Map<Key, DataPainter> cachedPainters;

    private MirurView currentView;
    private VariableObject currentData;
    private DataPainter currentPainter;

    @Override
    public void createPartControl(Composite parent) {
        viewSelectModel = new ViewSelectionModel();
        canvas = new NewtSwtGlimpseCanvas(parent, GLUtils.getDefaultGLProfile(), SWT.DOUBLE_BUFFERED);
        canvas.getGLDrawable().addGLEventListener(new GLCapabilityEventListener2());
        laf = new MirurLAF();
        animator = new FPSAnimator(canvas.getGLDrawable(), 20);
        animator.start();

        cachedPainters = Collections.synchronizedMap(new HashMap<Key, DataPainter>());

        resetAction = new ResetAxesAction() {
            @Override
            public void run() {
                if (currentPainter != null) {
                    currentPainter.resetAxes();
                }
            }
        };
        viewMenuAction = new ViewMenuAction() {
            @Override
            public Menu getMenu(Menu parent) {
                if (currentPainter != null) {
                    currentPainter.populateConfigMenu(parent);
                }

                return parent;
            }
        };
        saveArrayAction = new SaveArrayToFileAction();
        selectListenerToggle = new SelectListenerToggle(this, this);
        DuplicateViewAction duplicateViewAction = new DuplicateViewAction(ID);
        pinPaintersAction = new PinPaintersMenuAction() {
            @Override
            protected void select(VariableObject obj, DataPainter painter) {
                variableSelected(obj);
                // XXX want to preserve state of the painter
            }

            @Override
            protected Pair<VariableObject, DataPainter> getCurrent() {
                if (currentData == null || currentPainter == invalidPlaceholder) {
                    return null;
                } else {
                    return new Pair<>(currentData, currentPainter);
                }
            }
        };

        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        tbm.add(new SelectViewAction(viewSelectModel));
        tbm.add(viewMenuAction);
        tbm.add(resetAction);
        tbm.add(saveArrayAction);
        tbm.add(pinPaintersAction);
        tbm.add(duplicateViewAction);
        tbm.add(selectListenerToggle);

        getSite().getPage().addPartListener(selectListenerToggle);

        invalidPlaceholder = new InvalidPlaceholderView();

        viewSelectModel.addArrayListener(this);
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

        if (currentPainter != null) {
            currentPainter.detach(canvas);
            currentPainter.dispose(canvas);
        }

        super.dispose();
    }

    @Override
    public void variableSelected(VariableObject obj) {
        currentData = obj;
        pinPaintersAction.variableSelected(obj);
        viewSelectModel.variableSelected(obj);
        saveArrayAction.variableSelected(obj);
        refreshDataAndPainter();
    }

    @Override
    public void clearVariableCacheData() {
        synchronized (cachedPainters) {
            for (DataPainter p : cachedPainters.values()) {
                p.dispose(canvas);
            }

            cachedPainters.clear();
        }
    }

    @Override
    public void viewSelected(MirurView view) {
        currentView = view;
        refreshDataAndPainter();
    }

    private void refreshDataAndPainter() {
        animator.pause();

        if (currentPainter != null) {
            currentPainter.detach(canvas);
        }

        boolean hasValidPainter = false;
        if (currentData != null && currentView != null && currentView.supportsData(currentData)) {
            currentPainter = getOrCreatePainter(currentData, currentView);
            hasValidPainter = true;
        } else {
            currentPainter = invalidPlaceholder.create(canvas, currentData);
        }

        resetAction.setEnabled(hasValidPainter);
        viewMenuAction.setEnabled(hasValidPainter);

        currentPainter.attach(canvas);
        canvas.setLookAndFeel(laf);
        animator.resume();
    }

    private DataPainter getOrCreatePainter(VariableObject var, MirurView view) {
        Key key = new Key(var, view);
        DataPainter p = cachedPainters.get(key);

        if (p == null) {
            p = currentView.create(canvas, currentData);
            cachedPainters.put(key, p);
        }

        return p;
    }

    private static class Key {
        final VariableObject var;
        final MirurView view;

        Key(VariableObject var, MirurView view) {
            this.var = var;
            this.view = view;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((var == null) ? 0 : var.hashCode());
            result = prime * result + ((view == null) ? 0 : view.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (obj instanceof Key) {
                Key other = (Key) obj;
                return other.var.equals(var) && other.view.equals(view);
            }

            return true;
        }
    }
}

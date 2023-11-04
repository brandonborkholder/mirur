/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logWarning;
import static mirur.plugin.Activator.getVariableCache;
import static mirur.plugin.Activator.getVariableSelectionModel;

import java.util.Arrays;
import java.util.logging.Logger;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.model.IIndexedValue;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.internal.ui.views.variables.IndexedVariablePartition;
import org.eclipse.debug.ui.AbstractDebugView;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.DebugContextEvent;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.debug.ui.contexts.IDebugContextService;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaReferenceType;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.core.model.JDIArrayEntryVariable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.metsci.glimpse.util.StringUtils;

import mirur.core.VariableObject;

@SuppressWarnings("restriction")
public class VariableSelectListener implements ISelectionListener, ISelectionChangedListener, INullSelectionListener, IDebugEventSetListener,
IDebugContextListener {
    private static final Logger LOGGER = Logger.getLogger(VariableSelectListener.class.getName());

    private static final String VARIABLE_VIEW_ID = "org.eclipse.debug.ui.VariableView";

    public void install(IWorkbenchWindow window) {
        DebugPlugin.getDefault().addDebugEventListener(this);

        IDebugContextService service = DebugUITools.getDebugContextManager().getContextService(window);
        service.addDebugContextListener(this);

        IViewPart view = window.getActivePage().findView(VARIABLE_VIEW_ID);
        if (view == null) {
            // if the variables view isn't active
            window.getSelectionService().addPostSelectionListener(VARIABLE_VIEW_ID, this);
        } else {
            ((AbstractDebugView) view).getViewer().addSelectionChangedListener(this);
        }
    }

    public void uninstall(IWorkbenchWindow window) {
        DebugPlugin.getDefault().removeDebugEventListener(this);

        IDebugContextService service = DebugUITools.getDebugContextManager().getContextService(window);
        service.removeDebugContextListener(this);

        window.getSelectionService().removePostSelectionListener(VARIABLE_VIEW_ID, this);
        IViewPart view = window.getActivePage().findView(VARIABLE_VIEW_ID);
        if (view != null) {
            ((AbstractDebugView) view).getViewer().removeSelectionChangedListener(this);
        }

        getVariableSelectionModel().fireClearVariableCacheData();
        getVariableCache().clear();
    }

    private void update() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IDebugContextService service = DebugUITools.getDebugContextManager().getContextService(window);
        ISelection contextSelection = service.getActiveContext();
        IStackFrame frame = extract(contextSelection, IStackFrame.class);

        IVariable variable = null;
        IViewPart view = window.getActivePage().findView(VARIABLE_VIEW_ID);
        String name = "Mirur Object";
        if (view instanceof AbstractDebugView) {
            ISelection varSelection = ((AbstractDebugView) view).getViewer().getSelection();
            variable = extract(varSelection, IVariable.class);

            if (variable != null && varSelection instanceof ITreeSelection) {
                TreePath[] paths = ((ITreeSelection) varSelection).getPathsFor(variable);
                try {
                    name = getVariableName(paths[0]);
                } catch (DebugException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    logWarning(LOGGER, "Could not get name for %s", ex, varSelection);
                }
            }
        }

        // check a bunch of pre-conditions
        if (frame == null ||
                variable == null ||
                !frame.getThread().isSuspended() ||
                !isValidRefType(variable)) {
            getVariableSelectionModel().select(null);
        } else {
            if (getVariableCache().contains(variable, frame)) {
                VariableObject obj = getVariableCache().getArray(variable, frame);
                getVariableSelectionModel().select(obj);
            } else {
                new SelectStrategyJob(name, variable, frame).schedule();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T extract(ISelection selection, Class<T> type) {
        if (selection instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) selection).getFirstElement();
            if (type.isInstance(firstElement)) {
                return (T) firstElement;
            }
        }

        return null;
    }

    private String getVariableName(TreePath path) throws DebugException, NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {

        String[] parts = new String[path.getSegmentCount()];
        Arrays.fill(parts, "");
        for (int i = path.getSegmentCount() - 1; i >= 0; i--) {
            IVariable var = (IVariable) path.getSegment(i);
            parts[i] = var.getName();

            if (var instanceof IJavaVariable && !(var instanceof JDIArrayEntryVariable)) {
                break;
            }
        }

        for (int i = 0; i < path.getSegmentCount(); i++) {
            IVariable var = (IVariable) path.getSegment(i);

            /*
             * Collapse when multiple partitions are expanded or a partition is expanded into an entry.
             */
            if (var instanceof IndexedVariablePartition &&
                    i < path.getSegmentCount() - 1 &&
                    (path.getSegment(i + 1) instanceof IndexedVariablePartition ||
                            path.getSegment(i + 1) instanceof JDIArrayEntryVariable)) {
                parts[i] = "";
            }
        }

        return StringUtils.join("", parts);
    }

    private boolean isValidRefType(IVariable var) {
        /*
         * The thing can't be plotted if it's a primitive or void
         */
        try {
            return (var instanceof IJavaVariable && ((IJavaVariable) var).getJavaType() instanceof IJavaReferenceType)
                    || (var instanceof IndexedVariablePartition) || (var.getValue() instanceof IIndexedValue);
        } catch (DebugException ex) {
            return false;
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        if (event.getSource() instanceof Viewer &&
                ((Viewer) event.getSource()).getControl().isFocusControl()) {
            /*
             * Only update if the control actively made the selection change.
             * If another view maximizes, the selection is lost for some reason
             * through no fault of the original view.
             */
            update();
        }
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        // use the preferred selection service, which is ISelectionChangedListener
        if (part instanceof AbstractDebugView) {
            part.getSite().getWorkbenchWindow().getSelectionService().removePostSelectionListener(VARIABLE_VIEW_ID, this);
            ((AbstractDebugView) part).getViewer().addSelectionChangedListener(this);
        }

        update();
    }

    @Override
    public void debugContextChanged(DebugContextEvent event) {
        if (event.getFlags() == DebugContextEvent.ACTIVATED) {
            update();
        }
    }

    @Override
    public void handleDebugEvents(DebugEvent[] events) {
        for (DebugEvent event : events) {
            int kind = event.getKind();

            if (kind == DebugEvent.TERMINATE && event.getSource() instanceof IJavaDebugTarget) {
                Activator.getAgentDeployer().clear((IJavaDebugTarget) event.getSource());
                getVariableSelectionModel().fireClearVariableCacheData();
                getVariableCache().clear();
            }
            if (kind == DebugEvent.RESUME && !event.isEvaluation()) {
                getVariableSelectionModel().fireClearVariableCacheData();
                getVariableCache().clear();
            }
        }
    }

    public void forceUpdateNotify() {
        update();
    }
}

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
package mirur.plugin.detailpane;

import static com.metsci.glimpse.util.logging.LoggerUtils.logWarning;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.ui.IDetailPane;
import org.eclipse.debug.ui.IDetailPaneFactory;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ArrayDetailPaneFactory implements IDetailPaneFactory {
    private static final Logger LOGGER = Logger.getLogger(ArrayDetailPaneFactory.class.getName());

    @Override
    public Set<String> getDetailPaneTypes(IStructuredSelection selection) {
        if (isValid(selection)) {
            return Collections.singleton(ArrayDetailPane.ID);
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public String getDefaultDetailPane(IStructuredSelection selection) {
        if (isValid(selection)) {
            return ArrayDetailPane.ID;
        } else {
            return null;
        }
    }

    static String toString(IStructuredSelection selection) {
        if (selection.size() != 1) {
            return "";
        }

        try {
            // we assume it's valid
            Object o = selection.getFirstElement();
            IJavaArray array = (IJavaArray) ((IJavaVariable) o).getValue();
            int numElements = 100;
            StringBuilder sb = new StringBuilder();
            boolean complete = visit(sb, array, numElements) >= 0;

            if (!complete) {
                sb.append(" ...");
            }

            return sb.toString();
        } catch (DebugException ex) {
            logWarning(LOGGER, "Error getting variable details", ex);
            return "";
        }
    }

    private static int visit(StringBuilder sb, IJavaArray array, int remaining) throws DebugException {
        int i = 0;
        sb.append("[");
        for (; i < array.getSize() && remaining > 0; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            IJavaValue val = array.getValue(i);
            if (val instanceof IJavaPrimitiveValue) {
                sb.append(val.getValueString());
                remaining--;
            } else if (val instanceof IJavaArray) {
                remaining = visit(sb, (IJavaArray) val, remaining);
            } else {
                throw new AssertionError();
            }
        }

        if (i < array.getSize()) {
            // did not complete
            remaining = -1;
        }

        if (remaining >= 0) {
            sb.append("]");
        }

        return remaining;
    }

    static boolean isValid(IStructuredSelection selection) {
        Object o = null;
        if (selection.size() == 1) {
            o = selection.getFirstElement();
        }

        try {
            if (o instanceof IJavaVariable) {
                IValue val = ((IJavaVariable) o).getValue();
                if (val instanceof IJavaArray) {
                    String sig = ((IJavaArray) val).getSignature();
                    return sig.endsWith("[D") ||
                            sig.endsWith("[B") ||
                            sig.endsWith("[C") ||
                            sig.endsWith("[S") ||
                            sig.endsWith("[F") ||
                            sig.endsWith("[J") ||
                            sig.endsWith("[I") ||
                            sig.endsWith("[Z");
                }
            }
        } catch (DebugException ex) {
            // ignore
        }

        return false;
    }

    @Override
    public IDetailPane createDetailPane(String paneID) {
        if (ArrayDetailPane.ID.equals(paneID)) {
            return new ArrayDetailPane();
        } else {
            return null;
        }
    }

    @Override
    public String getDetailPaneName(String paneID) {
        if (ArrayDetailPane.ID.equals(paneID)) {
            return ArrayDetailPane.NAME;
        } else {
            return null;
        }
    }

    @Override
    public String getDetailPaneDescription(String paneID) {
        if (ArrayDetailPane.ID.equals(paneID)) {
            return ArrayDetailPane.DESCRIPTION;
        } else {
            return null;
        }
    }
}

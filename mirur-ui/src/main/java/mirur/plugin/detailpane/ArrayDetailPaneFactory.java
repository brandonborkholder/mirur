package mirur.plugin.detailpane;

import java.util.Collections;
import java.util.Set;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.ui.IDetailPane;
import org.eclipse.debug.ui.IDetailPaneFactory;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ArrayDetailPaneFactory implements IDetailPaneFactory {
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

    private boolean isValid(IStructuredSelection selection) {
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

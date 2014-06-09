package testplugin.views;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.internal.debug.core.model.JDILocalVariable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

@SuppressWarnings("restriction")
public abstract class VariableSelectListener implements ISelectionListener, INullSelectionListener {
    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof TreeSelection) {
            Object o = ((TreeSelection) selection).getFirstElement();
            PrimitiveArray selected = null;

            if (o instanceof JDILocalVariable) {
                try {
                    String name = ((JDILocalVariable) o).getName();
                    Object array = toPrimitiveArray(((JDILocalVariable) o).getValue());

                    if (array != null) {
                        selected = new Array1DImpl(name, array);
                    }
                } catch (DebugException e) {
                    throw new RuntimeException(e);
                }
            }

            setSelection(selected);
        }
    }

    protected abstract void setSelection(PrimitiveArray selected);

    private Object toPrimitiveArray(IValue value) throws DebugException {
        if (!(value instanceof IJavaArray)) {
            return null;
        }

        IJavaArray val = (IJavaArray) value;
        int len = val.getLength();

        IJavaType componentType = ((IJavaArrayType) val.getJavaType()).getComponentType();
        switch (componentType.getName()) {
        case "int": {
            int[] v = new int[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getIntValue();
            }
            return v;
        }
        case "float": {
            float[] v = new float[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getFloatValue();
            }
            return v;
        }
        case "double": {
            double[] v = new double[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getDoubleValue();
            }
            return v;
        }
        case "short": {
            short[] v = new short[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getShortValue();
            }
            return v;
        }
        case "long": {
            long[] v = new long[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getLongValue();
            }
            return v;
        }
        case "byte": {
            byte[] v = new byte[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getByteValue();
            }
            return v;
        }
        case "char": {
            char[] v = new char[len];
            for (int i = 0; i < len; i++) {
                IJavaPrimitiveValue pv = (IJavaPrimitiveValue) val.getValue(i);
                v[i] = pv.getCharValue();
            }
            return v;
        }

        default:
            // array of some other type
            return null;
        }
    }
}

package mirur.plugin;

import static mirur.plugin.Activator.getSelectionModel;
import mirur.core.PrimitiveTest;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IIndexedValue;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;

public class CopyJDIArrayJob extends Job {
    private final IJavaVariable var;
    private final IIndexedValue value;
    private final IJavaStackFrame frame;

    public CopyJDIArrayJob(IJavaVariable var, IIndexedValue value, IJavaStackFrame frame) {
        super("Copy JDI Array");
        this.var = var;
        this.frame = frame;
        this.value = value;

        setPriority(SHORT);
        setSystem(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            Object arrayObject = toPrimitiveArray(var.getName(), value);
            new SubmitArrayToUIJob(var.getName(), var, frame, arrayObject).schedule();

            return Status.OK_STATUS;
        } catch (DebugException ex) {
            IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error copying array via JDI", ex);
            PluginLogSupport.error(getClass(), status.getMessage(), status.getException());
            getSelectionModel().select(null);
            return status;
        }
    }

    private Object toPrimitiveArray(String name, IIndexedValue value) throws DebugException {
        if (!(value instanceof IJavaArray)) {
            // TODO handle this case
            return null;
        }

        IJavaArray val = (IJavaArray) value;
        IJavaType componentType = ((IJavaArrayType) val.getJavaType()).getComponentType();

        Object arrayObject;
        if (PrimitiveTest.isPrimitiveName(componentType.getName())) {
            arrayObject = toPrimitiveArray1d(value);
        } else if ((componentType instanceof IJavaArrayType)
                && PrimitiveTest.isPrimitiveName(((IJavaArrayType) componentType).getComponentType().getName())) {
            arrayObject = toPrimitiveArray2d(value);
        } else {
            arrayObject = null;
        }

        return arrayObject;
    }

    private Object toPrimitiveArray2d(IValue value) throws DebugException {
        IJavaArray val = (IJavaArray) value;
        int len = val.getLength();

        IJavaType componentType = ((IJavaArrayType) val.getJavaType()).getComponentType();
        switch (componentType.getName()) {
        case "int[]": {
            int[][] v = new int[len][];
            for (int i = 0; i < len; i++) {
                int[] w = (int[]) toPrimitiveArray1d(val.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "float[]": {
            float[][] v = new float[len][];
            for (int i = 0; i < len; i++) {
                float[] w = (float[]) toPrimitiveArray1d(val.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "double[]": {
            double[][] v = new double[len][];
            for (int i = 0; i < len; i++) {
                double[] w = (double[]) toPrimitiveArray1d(val.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "long[]": {
            long[][] v = new long[len][];
            for (int i = 0; i < len; i++) {
                long[] w = (long[]) toPrimitiveArray1d(val.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "short[]": {
            short[][] v = new short[len][];
            for (int i = 0; i < len; i++) {
                short[] w = (short[]) toPrimitiveArray1d(val.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "char[]": {
            char[][] v = new char[len][];
            for (int i = 0; i < len; i++) {
                char[] w = (char[]) toPrimitiveArray1d(val.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "byte[]": {
            byte[][] v = new byte[len][];
            for (int i = 0; i < len; i++) {
                byte[] w = (byte[]) toPrimitiveArray1d(val.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "boolean[]": {
            boolean[][] v = new boolean[len][];
            for (int i = 0; i < len; i++) {
                boolean[] w = (boolean[]) toPrimitiveArray1d(val.getValue(i));
                v[i] = w;
            }
            return v;
        }

        default:
            return null;
        }
    }

    private Object toPrimitiveArray1d(IValue value) throws DebugException {
        IJavaArray val = (IJavaArray) value;
        int len = val.getLength();
        IJavaValue[] all = val.getValues();

        IJavaType componentType = ((IJavaArrayType) val.getJavaType()).getComponentType();
        switch (componentType.getName()) {
        case "int": {
            int[] v = new int[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) all[i]).getIntValue();
            }
            return v;
        }
        case "float": {
            float[] v = new float[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) all[i]).getFloatValue();
            }
            return v;
        }
        case "double": {
            double[] v = new double[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) all[i]).getDoubleValue();
            }
            return v;
        }
        case "short": {
            short[] v = new short[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) all[i]).getShortValue();
            }
            return v;
        }
        case "long": {
            long[] v = new long[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) all[i]).getLongValue();
            }
            return v;
        }
        case "byte": {
            byte[] v = new byte[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) all[i]).getByteValue();
            }
            return v;
        }
        case "char": {
            char[] v = new char[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) all[i]).getCharValue();
            }
            return v;
        }
        case "boolean": {
            boolean[] v = new boolean[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) all[i]).getBooleanValue();
            }
            return v;
        }

        default:
            // array of some other type
            return null;
        }
    }
}

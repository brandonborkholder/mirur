package mirur.plugin;

import static mirur.plugin.Model.MODEL;
import mirur.plugins.Array1DImpl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.internal.debug.core.model.JDILocalVariable;
import org.eclipse.swt.widgets.Display;

@SuppressWarnings("restriction")
public class CopyJDIArrayJob extends Job {
    private final SelectionCache cache;
    private final JDILocalVariable var;

    public CopyJDIArrayJob(SelectionCache cache, JDILocalVariable var) throws DebugException {
        super("Copying " + var.getName());
        this.cache = cache;

        this.var = var;

        setPriority(Job.SHORT);
        setUser(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            String name = var.getName();
            if (name.equals(cache.getCurrent())) {
                return Status.OK_STATUS;
            }

            PrimitiveArray array = cache.getArray(name);

            if (array == null) {
                array = toPrimitiveArray(name, var.getValue());
                cache.put(name, array);
            }

            cache.setCurrent(name);
            final PrimitiveArray result = array;
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    MODEL.select(result);
                }
            });
        } catch (DebugException ex) {
            throw new RuntimeException(ex);
        }

        return Status.OK_STATUS;
    }

    private PrimitiveArray toPrimitiveArray(String name, IValue value) throws DebugException {
        if (!(value instanceof IJavaArray)) {
            return null;
        }

        IJavaArray val = (IJavaArray) value;
        IJavaType componentType = ((IJavaArrayType) val.getJavaType()).getComponentType();
        switch (componentType.getName()) {
        case "int":
        case "float":
        case "boolean":
        case "double":
        case "char":
        case "long":
        case "short":
        case "byte": {
            Object o = toPrimitiveArray1d(value);
            return new Array1DImpl(name, o);
        }

        case "int[]":
        case "float[]":
        case "boolean[]":
        case "double[]":
        case "char[]":
        case "long[]":
        case "short[]":
        case "byte[]": {
            Object o = toPrimitiveArray2d(value);
            // TODO check for squareness
            return new Array2DSquare(name, o);
        }

        default:
            return null;
        }
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

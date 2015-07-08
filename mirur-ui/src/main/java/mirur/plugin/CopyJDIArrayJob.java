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

import static mirur.core.PrimitiveTest.isPrimitiveName;
import static mirur.plugin.Activator.getSelectionModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IIndexedValue;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaValue;

public class CopyJDIArrayJob extends Job {
    private final String name;
    private final IVariable var;
    private final IJavaStackFrame frame;

    public CopyJDIArrayJob(String name, IVariable var, IJavaStackFrame frame) {
        super("Copy JDI Array");
        this.name = name;
        this.var = var;
        this.frame = frame;

        setPriority(SHORT);
        setSystem(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            IValue value = var.getValue();
            Object arrayObject = null;
            if (value instanceof IIndexedValue) {
                arrayObject = toPrimitiveArray(var.getName(), (IIndexedValue) value);
            }

            new SubmitArrayToUIJob(name, var, frame, arrayObject).schedule();

            return Status.OK_STATUS;
        } catch (DebugException ex) {
            IStatus status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Error copying array via JDI", ex);
            PluginLogSupport.error(getClass(), status.getMessage(), status.getException());
            getSelectionModel().select(null);
            return status;
        }
    }

    private Object toPrimitiveArray(String name, IIndexedValue value) throws DebugException {
        Object arrayObject = null;

        String refTypeName = value.getReferenceTypeName();
        String primitiveName = refTypeName.substring(0, refTypeName.indexOf('['));

        boolean isPrimitive = isPrimitiveName(primitiveName);
        boolean is1d = refTypeName.equals(primitiveName + "[]");
        boolean is2d = refTypeName.equals(primitiveName + "[][]");

        if (isPrimitive) {
            if (value instanceof IJavaArray) {
                if (is1d) {
                    arrayObject = toPrimitiveArray1d(primitiveName, (IJavaArray) value);
                } else if (is2d) {
                    arrayObject = toPrimitiveArray2d(primitiveName, (IJavaArray) value);
                }
            } else {
                if (is1d) {
                    arrayObject = toPrimitiveArray1d(primitiveName, value);
                } else if (is2d) {
                    arrayObject = toPrimitiveArray2d(primitiveName, value);
                }
            }
        }

        return arrayObject;
    }

    private Object toPrimitiveArray2d(String primitiveName, IJavaArray value) throws DebugException {
        int len = value.getLength();

        switch (primitiveName) {
        case "int": {
            int[][] v = new int[len][];
            for (int i = 0; i < len; i++) {
                int[] w = (int[]) toPrimitiveArray1d(primitiveName, (IJavaArray) value.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "float": {
            float[][] v = new float[len][];
            for (int i = 0; i < len; i++) {
                float[] w = (float[]) toPrimitiveArray1d(primitiveName, (IJavaArray) value.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "double": {
            double[][] v = new double[len][];
            for (int i = 0; i < len; i++) {
                double[] w = (double[]) toPrimitiveArray1d(primitiveName, (IJavaArray) value.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "long": {
            long[][] v = new long[len][];
            for (int i = 0; i < len; i++) {
                long[] w = (long[]) toPrimitiveArray1d(primitiveName, (IJavaArray) value.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "short": {
            short[][] v = new short[len][];
            for (int i = 0; i < len; i++) {
                short[] w = (short[]) toPrimitiveArray1d(primitiveName, (IJavaArray) value.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "char": {
            char[][] v = new char[len][];
            for (int i = 0; i < len; i++) {
                char[] w = (char[]) toPrimitiveArray1d(primitiveName, (IJavaArray) value.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "byte": {
            byte[][] v = new byte[len][];
            for (int i = 0; i < len; i++) {
                byte[] w = (byte[]) toPrimitiveArray1d(primitiveName, (IJavaArray) value.getValue(i));
                v[i] = w;
            }
            return v;
        }
        case "boolean": {
            boolean[][] v = new boolean[len][];
            for (int i = 0; i < len; i++) {
                boolean[] w = (boolean[]) toPrimitiveArray1d(primitiveName, (IJavaArray) value.getValue(i));
                v[i] = w;
            }
            return v;
        }

        default:
            return null;
        }
    }

    private Object toPrimitiveArray1d(String primitiveName, IJavaArray value) throws DebugException {
        int len = value.getLength();
        IJavaValue[] all = value.getValues();

        switch (primitiveName) {
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

    private Object toPrimitiveArray2d(String primitiveName, IIndexedValue value) throws DebugException {
        int len = value.getSize();
        int offset = value.getInitialOffset();

        switch (primitiveName) {
        case "int": {
            int[][] v = new int[len][];
            for (int i = 0; i < len; i++) {
                int[] w = (int[]) toPrimitiveArray1d(primitiveName, (IIndexedValue) value.getVariable(offset + i).getValue());
                v[i] = w;
            }
            return v;
        }
        case "float": {
            float[][] v = new float[len][];
            for (int i = 0; i < len; i++) {
                float[] w = (float[]) toPrimitiveArray1d(primitiveName, (IIndexedValue) value.getVariable(offset + i).getValue());
                v[i] = w;
            }
            return v;
        }
        case "double": {
            double[][] v = new double[len][];
            for (int i = 0; i < len; i++) {
                double[] w = (double[]) toPrimitiveArray1d(primitiveName, (IIndexedValue) value.getVariable(offset + i).getValue());
                v[i] = w;
            }
            return v;
        }
        case "long": {
            long[][] v = new long[len][];
            for (int i = 0; i < len; i++) {
                long[] w = (long[]) toPrimitiveArray1d(primitiveName, (IIndexedValue) value.getVariable(offset + i).getValue());
                v[i] = w;
            }
            return v;
        }
        case "short": {
            short[][] v = new short[len][];
            for (int i = 0; i < len; i++) {
                short[] w = (short[]) toPrimitiveArray1d(primitiveName, (IIndexedValue) value.getVariable(offset + i).getValue());
                v[i] = w;
            }
            return v;
        }
        case "char": {
            char[][] v = new char[len][];
            for (int i = 0; i < len; i++) {
                char[] w = (char[]) toPrimitiveArray1d(primitiveName, (IIndexedValue) value.getVariable(offset + i).getValue());
                v[i] = w;
            }
            return v;
        }
        case "byte": {
            byte[][] v = new byte[len][];
            for (int i = 0; i < len; i++) {
                byte[] w = (byte[]) toPrimitiveArray1d(primitiveName, (IIndexedValue) value.getVariable(offset + i).getValue());
                v[i] = w;
            }
            return v;
        }
        case "boolean": {
            boolean[][] v = new boolean[len][];
            for (int i = 0; i < len; i++) {
                boolean[] w = (boolean[]) toPrimitiveArray1d(primitiveName, (IIndexedValue) value.getVariable(offset + i).getValue());
                v[i] = w;
            }
            return v;
        }

        default:
            return null;
        }
    }

    private Object toPrimitiveArray1d(String primitiveName, IIndexedValue value) throws DebugException {
        if (value instanceof IJavaArray) {
            // faster this way
            return toPrimitiveArray1d(primitiveName, (IJavaArray) value);
        }

        int len = value.getSize();
        int offset = value.getInitialOffset();

        IVariable[] vars = value.getVariables(offset, len);

        switch (primitiveName) {
        case "int": {
            int[] v = new int[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) vars[i].getValue()).getIntValue();
            }
            return v;
        }
        case "float": {
            float[] v = new float[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) vars[i].getValue()).getFloatValue();
            }
            return v;
        }
        case "double": {
            double[] v = new double[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) vars[i].getValue()).getDoubleValue();
            }
            return v;
        }
        case "short": {
            short[] v = new short[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) vars[i].getValue()).getShortValue();
            }
            return v;
        }
        case "long": {
            long[] v = new long[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) vars[i].getValue()).getLongValue();
            }
            return v;
        }
        case "byte": {
            byte[] v = new byte[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) vars[i].getValue()).getByteValue();
            }
            return v;
        }
        case "char": {
            char[] v = new char[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) vars[i].getValue()).getCharValue();
            }
            return v;
        }
        case "boolean": {
            boolean[] v = new boolean[len];
            for (int i = 0; i < len; i++) {
                v[i] = ((IJavaPrimitiveValue) vars[i].getValue()).getBooleanValue();
            }
            return v;
        }

        default:
            // array of some other type
            return null;
        }
    }
}

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
import static mirur.plugin.Activator.getVariableSelectionModel;

import java.util.Arrays;
import java.util.logging.Logger;

import org.chromium.debug.core.model.Value;
import org.chromium.sdk.JsValue;
import org.chromium.sdk.JsValue.Type;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IIndexedValue;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class ChromeDevToolsArrayJob extends Job {
    private static final Logger LOGGER = Logger.getLogger(ChromeDevToolsArrayJob.class.getName());

    private final String name;
    private final IVariable var;
    private final IStackFrame frame;

    public ChromeDevToolsArrayJob(String name, IVariable var, IStackFrame frame) {
        super("Copy Array");
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
            logWarning(LOGGER, "Error copying json array", ex);
            IStatus status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Error copying json array", ex);
            getVariableSelectionModel().select(null);
            return status;
        }
    }

    private Object toPrimitiveArray(String name, IIndexedValue value) throws DebugException {
        Object arrayObject = null;

        try {
            if (value.getReferenceTypeName().equals(Type.TYPE_ARRAY.name()) && value.getSize() > 0) {
                double[] doubleValues = new double[value.getSize()];
                int idx = 0;

                for (int i = 0; i < value.getSize(); i++) {
                    Value v = (Value) value.getVariable(i).getValue();
                    JsValue jsValue = v.getJsValue();
                    if (jsValue.getType().equals(Type.TYPE_NUMBER)) {
                        doubleValues[idx++] = Double.parseDouble(jsValue.getValueString());
                    }
                }

                if (idx < doubleValues.length) {
                    doubleValues = Arrays.copyOf(doubleValues, idx);
                }

                arrayObject = doubleValues;
            }
        } catch (NumberFormatException ex) {
            arrayObject = null;
        }

        return arrayObject;
    }
}

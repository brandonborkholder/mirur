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

import java.util.logging.Logger;

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

        /*
         * If something is an array of non-zero size, try converting values
         * one-by-one into doubles until we hit something that's not a number.
         * Then just abort the entire array.
         */

        try {
            if (value.getReferenceTypeName().equals("TYPE_ARRAY") && value.getSize() > 0) {
                double[] doubleValues = new double[value.getSize()];

                for (int i = 0; i < value.getSize(); i++) {
                    IValue v = value.getVariable(i).getValue();
                    doubleValues[i] = Double.parseDouble(v.getValueString());
                }

                arrayObject = doubleValues;
            }
        } catch (NumberFormatException ex) {
            arrayObject = null;
        }

        return arrayObject;
    }
}

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

import static mirur.core.PrimitiveTest.isPrimitiveArray1d;
import static mirur.core.PrimitiveTest.isPrimitiveArray2d;
import static mirur.core.VisitArray.visit1d;
import static mirur.core.VisitArray.visit2d;
import static mirur.plugin.Activator.getStatistics;
import static mirur.plugin.Activator.getVariableCache;
import static mirur.plugin.Activator.getVariableSelectionModel;
import mirur.core.Array1DImpl;
import mirur.core.Array2DJagged;
import mirur.core.Array2DRectangular;
import mirur.core.IsJaggedVisitor;
import mirur.core.IsValidArrayVisitor;
import mirur.core.VariableObject;
import mirur.core.VariableObjectImpl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaStackFrame;

public class SubmitArrayToUIJob extends Job {
    private final String name;
    private final IVariable var;
    private final IJavaStackFrame frame;
    private final Object object;

    public SubmitArrayToUIJob(String name, IVariable var, IJavaStackFrame frame, Object object) {
        super("Sending Mirur Data to UI");
        this.name = name;
        this.var = var;
        this.frame = frame;
        this.object = object;

        setPriority(INTERACTIVE);
        setSystem(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        VariableObject varObject = null;

        if (object == null) {
            // leave null
        } else if (object instanceof VariableObject) {
            varObject = (VariableObject) object;
        } else if (isPrimitiveArray1d(object.getClass())) {
            if (visit1d(object, new IsValidArrayVisitor()).isValid()) {
                varObject = new Array1DImpl(name, object);
            }
        } else if (isPrimitiveArray2d(object.getClass())) {
            if (visit2d(object, new IsValidArrayVisitor()).isValid()) {
                if (visit2d(object, new IsJaggedVisitor()).isJagged()) {
                    varObject = new Array2DJagged(name, object);
                } else {
                    varObject = new Array2DRectangular(name, object);
                }
            }
        } else {
            varObject = new VariableObjectImpl(name, object);
        }

        if (varObject != null) {
            getStatistics().receivedFromTarget(varObject);
        }

        getVariableCache().put(var, frame, varObject);
        getVariableSelectionModel().select(varObject);

        return Status.OK_STATUS;
    }
}

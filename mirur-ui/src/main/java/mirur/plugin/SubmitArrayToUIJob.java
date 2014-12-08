package mirur.plugin;

import static mirur.core.PrimitiveTest.isPrimitiveArray1d;
import static mirur.core.PrimitiveTest.isPrimitiveArray2d;
import static mirur.core.VisitArray.visit1d;
import static mirur.core.VisitArray.visit2d;
import static mirur.plugin.Activator.getSelectionModel;
import static mirur.plugin.Activator.getStatistics;
import static mirur.plugin.Activator.getVariableCache;
import mirur.core.Array1DImpl;
import mirur.core.Array2DJagged;
import mirur.core.Array2DRectangular;
import mirur.core.IsJaggedVisitor;
import mirur.core.IsValidArrayVisitor;
import mirur.core.PrimitiveArray;

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
    private final Object arrayObject;

    public SubmitArrayToUIJob(String name, IVariable var, IJavaStackFrame frame, Object arrayObject) {
        super("Sending Mirur Array Data to UI");
        this.name = name;
        this.var = var;
        this.frame = frame;
        this.arrayObject = arrayObject;

        setPriority(INTERACTIVE);
        setSystem(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        PrimitiveArray array = null;

        if (arrayObject == null) {
            // leave null
        } else if (isPrimitiveArray1d(arrayObject.getClass())) {
            if (visit1d(arrayObject, new IsValidArrayVisitor()).isValid()) {
                array = new Array1DImpl(name, arrayObject);
            }
        } else if (isPrimitiveArray2d(arrayObject.getClass())) {
            if (visit2d(arrayObject, new IsValidArrayVisitor()).isValid()) {
                if (visit2d(arrayObject, new IsJaggedVisitor()).isJagged()) {
                    array = new Array2DJagged(name, arrayObject);
                } else {
                    array = new Array2DRectangular(name, arrayObject);
                }
            }
        }

        if (array != null) {
            getStatistics().receivedFromTarget(array);
        }

        getVariableCache().put(var, frame, array);
        getSelectionModel().select(array);

        return Status.OK_STATUS;
    }
}

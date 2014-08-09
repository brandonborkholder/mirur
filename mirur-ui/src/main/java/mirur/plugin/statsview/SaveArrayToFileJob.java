package mirur.plugin.statsview;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import mirur.core.Array1D;
import mirur.core.Array2D;
import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;
import mirur.plugin.Activator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class SaveArrayToFileJob extends Job {
    private final PrimitiveArray array;
    private final File dest;

    public SaveArrayToFileJob(PrimitiveArray array, File dest) {
        super("Saving " + array.getName() + " to " + dest);
        this.array = array;
        this.dest = dest;

        setPriority(Job.SHORT);
        setUser(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try (PrintStream out = new PrintStream(dest)) {
            if (array instanceof Array1D) {
                VisitArray.visit1d(array.getData(), new Array1dToCsvVisitor(out, getName(), monitor));
            } else if (array instanceof Array2D) {
                VisitArray.visit2d(array.getData(), new Array2dToCsvVisitor(out, getName(), monitor));
            } else {
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Array is not 1 or 2 dimensions");
            }

            monitor.done();
            out.close();
        } catch (IOException ex) {
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to write " + array.getName() + " to file " + dest, ex);
        } catch (OperationCanceledException ex) {
            return Status.CANCEL_STATUS;
        }

        return Status.OK_STATUS;
    }
}

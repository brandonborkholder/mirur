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
package mirur.plugin.statsview;

import static com.metsci.glimpse.util.logging.LoggerUtils.logWarning;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import mirur.core.Array1D;
import mirur.core.Array2D;
import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;
import mirur.plugin.Activator;

public class SaveArrayToFileJob extends Job {
    private static final Logger LOGGER = Logger.getLogger(SaveArrayToFileJob.class.getName());

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
        try (PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(dest)))) {
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
            logWarning(LOGGER, "Failed to write array to file", ex);
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to write " + array.getName() + " to file " + dest, ex);
        } catch (OperationCanceledException ex) {
            return Status.CANCEL_STATUS;
        }

        return Status.OK_STATUS;
    }
}

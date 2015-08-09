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
import static mirur.plugin.Activator.getAgentDeployer;

import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;

public class InstallMirurAgentJob extends Job {
    private static final Logger LOGGER = Logger.getLogger(InstallMirurAgentJob.class.getName());

    private final IJavaStackFrame frame;

    public InstallMirurAgentJob(IJavaStackFrame frame) {
        super("Install Mirur Agent");

        this.frame = frame;

        setPriority(SHORT);
        setSystem(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            IJavaThread thread = (IJavaThread) frame.getThread();
            IJavaDebugTarget target = (IJavaDebugTarget) thread.getDebugTarget();

            getAgentDeployer().install(target, thread);
            return Status.OK_STATUS;
        } catch (VariableTransferException | DebugException ex) {
            logWarning(LOGGER, "Error installing agent", ex);
            IStatus status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Error installing agent", ex);
            return status;
        }
    }
}

package mirur.plugin;

import static mirur.plugin.Activator.getAgentDeployer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;

public class InstallMirurAgentJob extends Job {
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
            IStatus status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Error installing agent", ex);
            PluginLogSupport.error(getClass(), status.getMessage(), status.getException());
            return status;
        }
    }
}

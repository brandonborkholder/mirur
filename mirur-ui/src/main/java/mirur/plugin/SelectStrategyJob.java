package mirur.plugin;

import static mirur.plugin.Activator.getAgentDeployer;
import static mirur.plugin.Activator.getSelectionModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaVariable;

public class SelectStrategyJob extends Job {
    private final String name;
    private final IVariable var;
    private final IJavaStackFrame frame;

    public SelectStrategyJob(String name, IVariable var, IJavaStackFrame frame) {
        super("Select Variable Transfer Strategy");
        this.name = name;
        this.var = var;
        this.frame = frame;

        setPriority(INTERACTIVE);
        setSystem(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            execute();

            return Status.OK_STATUS;
        } catch (DebugException | InterruptedException ex) {
            IStatus status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Exception in selecting variable transfer strategy", ex);
            PluginLogSupport.error(getClass(), status.getMessage(), status.getException());
            getSelectionModel().select(null);
            return status;
        }
    }

    private void execute() throws DebugException, InterruptedException {
        IJavaDebugTarget target = (IJavaDebugTarget) frame.getDebugTarget();

        if (var instanceof IJavaVariable) {
            IJavaVariable jvar = (IJavaVariable) var;

            if (!getAgentDeployer().isAgentInstallAttempted(target)) {
                // agent install not attempted yet
                InstallMirurAgentJob installJob = new InstallMirurAgentJob(frame);
                installJob.schedule();
                installJob.join();
            }

            if (getAgentDeployer().isAgentInstalled(target)) {
                IJavaClassType agentType = getAgentDeployer().getAgentClass(target);
                new ReceiveArrayJob(name, jvar, frame, agentType).schedule();
            } else {
                new CopyJDIArrayJob(name, var, frame).schedule();
            }
        } else {
            new CopyJDIArrayJob(name, var, frame).schedule();
        }
    }
}

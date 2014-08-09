package mirur.plugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaReferenceType;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaVariable;

public abstract class InvokeRemoteMethodJob extends Job {
    protected final IJavaVariable var;
    protected final IJavaStackFrame frame;

    protected IJavaDebugTarget target;
    protected IJavaThread thread;

    public InvokeRemoteMethodJob(String name, IJavaVariable var, IJavaStackFrame frame) {
        super(name);

        this.frame = frame;
        this.var = var;

        setPriority(Job.SHORT);
        setUser(false);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        thread = (IJavaThread) frame.getThread();
        target = (IJavaDebugTarget) thread.getDebugTarget();

        try {
            if (thread.isSuspended() && var.getJavaType() instanceof IJavaReferenceType) {
                IJavaClassType agentType = Activator.getAgentDeployer().install(target, thread);

                thread.queueRunnable(new AgentInvokeRunnable(agentType));
            } else {
                failed(null);
            }
        } catch (DebugException ex) {
            failed(ex);
            throw new VariableTransferException(ex);
        } catch (VariableTransferException ex) {
            failed(ex);
            throw ex;
        }

        return Status.OK_STATUS;
    }

    protected void failed(Exception ex) {
        Activator.getSelectionModel().select(null);
    }

    protected abstract void invokeAgent(IJavaClassType agentType) throws Exception;

    private class AgentInvokeRunnable implements Runnable {
        final IJavaClassType agentType;

        AgentInvokeRunnable(IJavaClassType agentType) {
            this.agentType = agentType;
        }

        @Override
        public void run() {
            try {
                invokeAgent(agentType);
            } catch (Exception ex) {
                failed(ex);
                throw new VariableTransferException(ex);
            }
        }
    }
}

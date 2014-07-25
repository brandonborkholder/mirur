package mirur.plugin;

import static org.eclipse.jdt.internal.debug.core.JavaDebugUtils.resolveJavaElement;

import java.io.IOException;

import mirur.core.MirurAgent;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaReferenceType;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;

@SuppressWarnings("restriction")
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
            IJavaProject project = resolveJavaProject();

            if (project != null && thread.isSuspended() && var.getJavaType() instanceof IJavaReferenceType) {
                new RemoteAgentDeployer().install(target, project);

                thread.queueRunnable(new AgentInvokeRunnable());
            } else {
                failed(null);
            }
        } catch (IOException | VariableTransferException | CoreException ex) {
            failed(ex);
            throw new VariableTransferException(ex);
        }

        return Status.OK_STATUS;
    }

    protected void failed(Exception ex) {
        Activator.getSelectionModel().select(null);
    }

    protected abstract void invokeAgent(IJavaClassType agentType) throws Exception;

    private IJavaProject resolveJavaProject() throws CoreException {
        ILaunch launch = frame.getLaunch();
        // TODO this should try to find the main class and get that project
        // (this frame could be in the JDK)
        IJavaElement element = resolveJavaElement(frame, launch);
        if (element == null) {
            return null;
        } else {
            return element.getJavaProject();
        }
    }

    private IJavaClassType getRemoteAgentClass() throws DebugException {
        IJavaType[] types = target.getJavaTypes(MirurAgent.class.getName());
        if (types == null) {
            loadRemoteAgentClass(target, thread);
            types = target.getJavaTypes(MirurAgent.class.getName());
        }

        return (IJavaClassType) types[0];
    }

    private void loadRemoteAgentClass(IJavaDebugTarget target, IJavaThread thread) throws DebugException {
        IJavaType[] types = target.getJavaTypes(Class.class.getName());
        IJavaClassType classClass = (IJavaClassType) types[0];
        IJavaValue[] args = new IJavaValue[] { target.newValue(MirurAgent.class.getName()) };
        classClass.sendMessage("forName", "(Ljava/lang/String;)Ljava/lang/Class;", args, thread);
    }

    private class AgentInvokeRunnable implements Runnable {
        @Override
        public void run() {
            try {
                invokeAgent(getRemoteAgentClass());
            } catch (Exception ex) {
                failed(ex);
                throw new VariableTransferException(ex);
            }
        }
    }
}

package mirur.plugin;

import static org.eclipse.jdt.internal.debug.core.JavaDebugUtils.resolveJavaProject;

import java.util.Arrays;

import mirur.core.MirurAgent;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.core.IJavaClassObject;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.debug.eval.IEvaluationListener;
import org.eclipse.jdt.debug.eval.IEvaluationResult;

@SuppressWarnings("restriction")
public class InvokeRemoteMethodJob extends Job implements IEvaluationListener {
    private final IJavaVariable var;
    private final IJavaStackFrame frame;

    public InvokeRemoteMethodJob(IJavaVariable var, IJavaStackFrame frame) {
        super("invokeing method");
        this.frame = frame;
        this.var = var;

        setPriority(Job.SHORT);
        setUser(false);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        IJavaThread thread = (IJavaThread) frame.getThread();
        IDebugTarget target = thread.getDebugTarget();
        IJavaProject project = resolveJavaProject(frame);

        if (target instanceof IJavaDebugTarget && project != null && thread.isSuspended()) {
            deployAgent(project);

            try {
                IJavaClassType agentType = getRemoteAgentClass((IJavaDebugTarget) target, thread);
                IJavaValue[] args = new IJavaValue[] { (IJavaValue) var.getValue() };
                IJavaValue result = agentType.sendMessage("test", "(Ljava/lang/Object;)Ljava/lang/Object;", args, thread);
                System.out.println(result.getValueString());
            } catch (DebugException ex) {
                throw new RuntimeException(ex);
            }
        }

        return Status.OK_STATUS;
    }

    private void deployAgent(IJavaProject project) {
        new RemoteAgentDeployer().install(project);
    }

    private IJavaClassType getRemoteAgentClass(IJavaDebugTarget target, IJavaThread thread) throws DebugException {
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

    @Override
    public void evaluationComplete(IEvaluationResult result) {
        if (result.hasErrors()) {
            System.err.println(Arrays.toString(result.getErrorMessages()));
            if (result.getException() != null) {
                result.getException().printStackTrace();
            }
        } else {
            try {
                System.out.println(result.getValue().getValueString());
            } catch (DebugException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

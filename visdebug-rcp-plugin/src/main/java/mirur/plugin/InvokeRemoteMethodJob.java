package mirur.plugin;

import static org.eclipse.jdt.internal.debug.core.JavaDebugUtils.resolveJavaProject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.debug.eval.IAstEvaluationEngine;
import org.eclipse.jdt.debug.eval.ICompiledExpression;
import org.eclipse.jdt.debug.eval.IEvaluationListener;
import org.eclipse.jdt.debug.eval.IEvaluationResult;
import org.eclipse.jdt.internal.debug.core.JDIDebugPlugin;

@SuppressWarnings("restriction")
public class InvokeRemoteMethodJob extends Job {
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

        IJavaProject proj = resolveJavaProject(frame);
        if (target instanceof IJavaDebugTarget) {
            IAstEvaluationEngine eval = JDIDebugPlugin.getDefault().getEvaluationEngine(proj, (IJavaDebugTarget) target);

            try {
                ICompiledExpression expr = eval.getCompiledExpression("java.lang.System.out.println(java.util.Arrays.toString(" + var.getName() + "));", frame);
                IEvaluationListener listener = new IEvaluationListener() {
                    @Override
                    public void evaluationComplete(IEvaluationResult result) {
                        if (result.hasErrors()) {
                            result.getException().printStackTrace();
                        } else {
                            IJavaValue value = result.getValue();
                            try {
                                System.out.println(value.getValueString());
                            } catch (DebugException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                };
                eval.evaluateExpression(expr, frame, listener, DebugEvent.EVALUATION_IMPLICIT, false);
            } catch (DebugException ex) {
                ex.printStackTrace();
            }
        }

        return Status.OK_STATUS;
    }
}

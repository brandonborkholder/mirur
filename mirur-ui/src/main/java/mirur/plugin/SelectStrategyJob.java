package mirur.plugin;

import static mirur.plugin.Activator.getAgentDeployer;
import static mirur.plugin.Activator.getSelectionModel;
import mirur.core.PrimitiveTest;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IIndexedValue;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaVariable;

public class SelectStrategyJob extends Job {
    private final IJavaVariable var;
    private final IJavaStackFrame frame;

    public SelectStrategyJob(IJavaVariable var, IJavaStackFrame frame) {
        super("Select Variable Transfer Strategy");
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
            IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Exception in selecting variable transfer strategy", ex);
            PluginLogSupport.error(getClass(), status.getMessage(), status.getException());
            getSelectionModel().select(null);
            return status;
        }
    }

    private void execute() throws DebugException, InterruptedException {
        IJavaDebugTarget target = (IJavaDebugTarget) frame.getDebugTarget();

        if (getAgentDeployer().isAgentInstalled(target)) {
            // agent is installed
            IJavaClassType agentType = getAgentDeployer().getAgentClass(target);
            new ReceiveArrayJob(var, frame, agentType).schedule();

        } else if (getAgentDeployer().isAgentInstallFailed(target) && isValidTypeForJDI(var)) {
            // agent install attempted and failed
            new CopyJDIArrayJob(var, (IIndexedValue) var.getValue(), frame).schedule();

        } else {
            // agent install not attempted yet
            InstallMirurAgentJob installJob = new InstallMirurAgentJob(frame);
            installJob.schedule();
            installJob.join();

            // success or fail, we'll find out here

            if (getAgentDeployer().isAgentInstalled(target)) {
                IJavaClassType agentType = getAgentDeployer().getAgentClass(target);
                new ReceiveArrayJob(var, frame, agentType).schedule();
            } else {
                new CopyJDIArrayJob(var, (IIndexedValue) var.getValue(), frame).schedule();
            }
        }
    }

    private boolean isValidTypeForJDI(IJavaVariable var) throws DebugException {
        IValue value = var.getValue();

        if (value instanceof IIndexedValue) {
            String refTypeName = ((IIndexedValue) value).getReferenceTypeName();
            // XXX handle this
            System.out.println(refTypeName);
        } else if (value instanceof IJavaArray) {
            IJavaArrayType arrayType = (IJavaArrayType) ((IJavaArray) value).getJavaType();
            IJavaType componentType = arrayType.getComponentType();
            if (PrimitiveTest.isPrimitiveName(componentType.getName())) {
                return true;
            } else if (componentType instanceof IJavaArrayType) {
                IJavaType component2Type = ((IJavaArrayType) componentType).getComponentType();
                return PrimitiveTest.isPrimitiveName(component2Type.getName());
            }
        }

        return false;
    }

}

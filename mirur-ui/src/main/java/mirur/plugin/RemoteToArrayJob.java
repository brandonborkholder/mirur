package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logFine;

import java.util.logging.Logger;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;

public class RemoteToArrayJob extends InvokeRemoteMethodJob {
    private static final Logger LOGGER = Logger.getLogger(RemoteToArrayJob.class.getName());

    public RemoteToArrayJob(IJavaVariable var, IJavaStackFrame frame) {
        super("Converting Object to Array", var, frame);
    }

    @Override
    protected void invokeAgent(IJavaClassType agentType) throws DebugException, VariableTransferException {
        IJavaValue value = (IJavaValue) var.getValue();
        IJavaValue[] args = new IJavaValue[] { value };
        IJavaValue result = agentType.sendMessage("toArray", "(Ljava/lang/Object;)Ljava/lang/Object;", args, thread);
        logFine(LOGGER, "Called MirurAgent.toArray(Object) successfully");

        String originalObjectSignature = value.getSignature();
        Activator.getStatistics().transformedViaAgent(originalObjectSignature);

        String name = var.getName();

        if (result instanceof IJavaArray) {
            new CopyJDIArrayJob(name, (IJavaArray) result, frame).schedule();
        } else if (result.isNull()) {
            Activator.getVariableCache().put(name, frame, null);
            Activator.getSelectionModel().select(null);
        } else {
            failed(null);
        }
    }
}

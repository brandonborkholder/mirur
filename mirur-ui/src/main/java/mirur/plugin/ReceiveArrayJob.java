package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logFine;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import mirur.core.Array1DImpl;
import mirur.core.Array2DJagged;
import mirur.core.Array2DRectangular;
import mirur.core.IsJaggedVisitor;
import mirur.core.PrimitiveArray;
import mirur.core.PrimitiveTest;
import mirur.core.VisitArray;

import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;

public class ReceiveArrayJob extends InvokeRemoteMethodJob {
    private static final Logger LOGGER = Logger.getLogger(ReceiveArrayJob.class.getName());

    public ReceiveArrayJob(String name, IJavaVariable var, IJavaStackFrame frame) {
        super("Receiving Array", var, frame);
    }

    @Override
    protected void invokeAgent(IJavaClassType agentType) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(null);
        int port = serverSocket.getLocalPort();

        CyclicBarrier barrier = new CyclicBarrier(2);

        logFine(LOGGER, "Waiting to receive array on port %d", port);

        FutureTask<Object> socketTask = new FutureTask<>(new IncomingConnectionTask(barrier, serverSocket));
        new Thread(socketTask, "mirur-socket-listen").start();

        // TODO I really don't like this method of waiting until the socket is listening
        barrier.await();

        IJavaValue portValue = target.newValue(port);
        IJavaValue value = (IJavaValue) var.getValue();
        IJavaValue[] args = new IJavaValue[] { value, portValue };
        agentType.sendMessage("sendAsArray", "(Ljava/lang/Object;I)V", args, thread);
        logFine(LOGGER, "Called MirurAgent.sendAsArray(Object, int) successfully");

        Object arrayObject = socketTask.get();

        PrimitiveArray array;
        if (arrayObject == null) {
            array = null;
            Activator.getSelectionModel().select(null);
        } else if (PrimitiveTest.isPrimitiveArray1d(arrayObject.getClass())) {
            Activator.getStatistics().transformedViaAgent(var.getGenericSignature());

            array = new Array1DImpl(var.getName(), arrayObject);
            Activator.getStatistics().receivedFromTarget(array);
        } else if (PrimitiveTest.isPrimitiveArray2d(arrayObject.getClass())) {
            Activator.getStatistics().transformedViaAgent(var.getGenericSignature());

            if (VisitArray.visit2d(arrayObject, new IsJaggedVisitor()).isJagged()) {
                array = new Array2DJagged(var.getName(), arrayObject);
            } else {
                array = new Array2DRectangular(var.getName(), arrayObject);
            }

            Activator.getStatistics().receivedFromTarget(array);
        } else {
            array = null;
        }

        Activator.getVariableCache().put(var, frame, array);
        Activator.getSelectionModel().select(array);
    }

    private class IncomingConnectionTask implements Callable<Object> {
        final CyclicBarrier barrier;
        final ServerSocket serverSocket;

        IncomingConnectionTask(CyclicBarrier barrier, ServerSocket serverSocket) {
            this.barrier = barrier;
            this.serverSocket = serverSocket;
        }

        @Override
        public Object call() throws Exception {
            try {
                barrier.await();
                Socket sock = serverSocket.accept();

                InputStream in = sock.getInputStream();
                ObjectInputStream objIn = new ObjectInputStream(in);
                Object value = objIn.readObject();
                objIn.close();

                return value;
            } finally {
                serverSocket.close();
            }
        }
    }
}

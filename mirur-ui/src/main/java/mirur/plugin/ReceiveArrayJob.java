package mirur.plugin;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;

import mirur.core.Array1D;
import mirur.core.Array1DImpl;
import mirur.core.Array2D;
import mirur.core.Array2DRectangular;
import mirur.core.PrimitiveTest;

import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;

public class ReceiveArrayJob extends InvokeRemoteMethodJob {
    public ReceiveArrayJob(String name, IJavaVariable var, IJavaStackFrame frame) {
        super("Receiving Array", var, frame);
    }

    @Override
    protected void invokeAgent(IJavaClassType agentType) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(null);
        int port = serverSocket.getLocalPort();

        CyclicBarrier barrier = new CyclicBarrier(2);

        FutureTask<Object> socketTask = new FutureTask<>(new IncomingConnectionTask(barrier, serverSocket));
        new Thread(socketTask, "mirur-socket-listen").start();

        // TODO I really don't like this method of waiting until the socket is listening
        barrier.await();

        IJavaValue portValue = target.newValue(port);

        IJavaValue[] args = new IJavaValue[] { (IJavaValue) var.getValue(), portValue };
        agentType.sendMessage("sendAsArray", "(Ljava/lang/Object;I)V", args, thread);

        Object value = socketTask.get();

        if (value == null) {
            Activator.getSelectionModel().select(null);
        } else if (PrimitiveTest.isPrimitiveArray1d(value.getClass())) {
            Array1D array = new Array1DImpl(var.getName(), value);
            Activator.getSelectionModel().select(array);
        } else if (PrimitiveTest.isPrimitiveArray2d(value.getClass())) {
            // TODO test if rectangular
            Array2D array = new Array2DRectangular(var.getName(), value);
            Activator.getSelectionModel().select(array);
        } else {
            Activator.getSelectionModel().select(null);
        }
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
            barrier.await();
            Socket sock = serverSocket.accept();

            InputStream in = sock.getInputStream();
            ObjectInputStream objIn = new ObjectInputStream(in);
            Object value = objIn.readObject();
            objIn.close();

            serverSocket.close();

            return value;
        }
    }
}

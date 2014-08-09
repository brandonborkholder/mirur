package mirur.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.concurrent.SynchronousQueue;

import mirur.core.MirurAgent;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaClassObject;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;

public class RemoteAgentDeployer {
    private File agentClassesDir;

    public IJavaClassType install(IJavaDebugTarget target, IJavaThread thread) throws VariableTransferException, DebugException {
        if (!isValidJVMVersion(target.getVersion())) {
            throw new VariableTransferException(VariableTransferException.ERR_Invalid_Jvm_Version);
        }

        synchronized (this) {
            if (agentClassesDir == null) {
                try {
                    agentClassesDir = explodeAgentClasses();
                } catch (IOException ex) {
                    throw new VariableTransferException(VariableTransferException.ERR_Could_Not_Write_Agent_Classes, ex);
                }
            }
        }

        SynchronousQueue<IJavaClassType> agentTypeQueue = new SynchronousQueue<>();
        thread.queueRunnable(new LoadAgentClasses(target, thread, MirurAgent.class.getName(), agentTypeQueue));

        try {
            return agentTypeQueue.take();
        } catch (InterruptedException ex) {
            throw new VariableTransferException(ex);
        }
    }

    private String[] getAgentClasses() {
        return new String[] { MirurAgent.class.getName() };
    }

    private File explodeAgentClasses() throws IOException {
        File tmpClasspathDir = Files.createTempDirectory("miruragent").toFile();

        for (String className : getAgentClasses()) {
            String asFile = className.replace('.', '/').concat(".class");
            File classFile = new File(tmpClasspathDir, asFile);
            File classFileParent = classFile.getParentFile();
            if (!classFileParent.exists()) {
                classFileParent.mkdirs();
            }

            writeClass(asFile, classFile);
            classFile.deleteOnExit();
        }

        return tmpClasspathDir;
    }

    private void writeClass(String classFile, File fileDest) throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(classFile)) {
            OutputStream out = new FileOutputStream(fileDest);
            byte[] buf = new byte[100];
            while (true) {
                int count = in.read(buf);
                if (count > 0) {
                    out.write(buf, 0, count);
                } else {
                    break;
                }
            }

            in.close();
            out.close();
        }
    }

    private boolean isValidJVMVersion(String version) {
        int secondDot = version.indexOf('.');
        secondDot = version.indexOf('.', secondDot + 1);
        String major = version.substring(0, secondDot);

        return Double.parseDouble(major) >= 1.5;
    }

    private IJavaClassType loadAgentClass(IJavaDebugTarget target, IJavaThread thread, File classpathDir, String agentClassName)
            throws DebugException, MalformedURLException {
        // get the types we need

        // java.net.URLClassLoader
        IJavaType[] types = target.getJavaTypes(URLClassLoader.class.getName());
        IJavaClassType classloaderType = (IJavaClassType) types[0];

        // java.lang.Class
        types = target.getJavaTypes(Class.class.getName());
        IJavaClassType classType = (IJavaClassType) types[0];

        // java.net.URL
        types = target.getJavaTypes(URL.class.getName());
        IJavaClassType urlType = (IJavaClassType) types[0];

        // java.lang.reflect.Array
        types = target.getJavaTypes(Array.class.getName());
        IJavaClassType arrayType = (IJavaClassType) types[0];

        // create the URL object we need
        // new URL(String)
        IJavaValue classNameString = target.newValue(classpathDir.toURI().toURL().toString());
        IJavaValue[] args = new IJavaValue[] { classNameString };
        // IJavaObject urlObject = (IJavaObject) urlType.sendMessage("<init>", "(Ljava/lang/String;)V", args, thread);
        IJavaObject urlObject = urlType.newInstance("(Ljava/lang/String;)V", args, thread);

        // create the URL[] array
        // Array.newInstance(Class,int)
        args = new IJavaValue[] { urlType.getClassObject(), target.newValue(1) };
        IJavaValue urlArrayObject = arrayType.sendMessage("newInstance", "(Ljava/lang/Class;I)Ljava/lang/Object;", args, thread);

        // set the value
        // Array.set(Object,int,Object)
        args = new IJavaValue[] { urlArrayObject, target.newValue(0), urlObject };
        arrayType.sendMessage("set", "(Ljava/lang/Object;ILjava/lang/Object;)V", args, thread);

        // create the URLClassLoader
        // URLClassLoader.newInstance(URL[])
        args = new IJavaValue[] { urlArrayObject };
        IJavaValue classLoaderObject = classloaderType.sendMessage("newInstance", "([Ljava/net/URL;)Ljava/net/URLClassLoader;", args, thread);

        // load the class
        // Class.forName(String,boolean,ClassLoader)
        args = new IJavaValue[] { target.newValue(agentClassName), target.newValue(true), classLoaderObject };
        IJavaValue classObject = classType.sendMessage("forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;", args, thread);

        return (IJavaClassType) ((IJavaClassObject) classObject).getInstanceType();
    }

    private class LoadAgentClasses implements Runnable {
        final IJavaDebugTarget target;
        final IJavaThread thread;
        final String agentClassName;
        final SynchronousQueue<IJavaClassType> agentClassQueue;

        LoadAgentClasses(IJavaDebugTarget target, IJavaThread thread, String agentClassName, SynchronousQueue<IJavaClassType> agentClassQueue) {
            this.target = target;
            this.thread = thread;
            this.agentClassName = agentClassName;
            this.agentClassQueue = agentClassQueue;
        }

        @Override
        public void run() {
            try {
                IJavaClassType agentType = loadAgentClass(target, thread, agentClassesDir, agentClassName);
                agentClassQueue.put(agentType);
            } catch (DebugException | MalformedURLException | InterruptedException ex) {
                throw new VariableTransferException(VariableTransferException.ERR_Could_Not_Load_Agent_In_Classloader, ex);
            }
        }
    }
}

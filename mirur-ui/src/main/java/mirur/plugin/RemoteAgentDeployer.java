/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © ${year} Brandon Borkholder (support@mirur.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logFine;
import static com.metsci.glimpse.util.logging.LoggerUtils.logFiner;

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
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaClassObject;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;

import mirur.core.MirurAgent;

public class RemoteAgentDeployer {
    private static final Logger LOGGER = Logger.getLogger(RemoteAgentDeployer.class.getName());

    private File agentClassesDir;

    private Map<IJavaDebugTarget, IJavaClassType> cache = Collections.synchronizedMap(new WeakHashMap<IJavaDebugTarget, IJavaClassType>());

    public IJavaClassType install(IJavaDebugTarget target, IJavaThread thread) throws VariableTransferException, DebugException {
        // mark as attempted
        cache.put(target, null);

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

        IJavaClassType agentType = loadRemoteAgent(target, thread);
        cache.put(target, agentType);

        return agentType;
    }

    public IJavaClassType getAgentClass(IJavaDebugTarget target) {
        return cache.get(target);
    }

    public boolean isAgentInstallAttempted(IJavaDebugTarget target) {
        return cache.containsKey(target);
    }

    public boolean isAgentInstallFailed(IJavaDebugTarget target) {
        return cache.get(target) == null;
    }

    public boolean isAgentInstalled(IJavaDebugTarget target) {
        return cache.get(target) != null;
    }

    public void clear(IJavaDebugTarget target) {
        cache.remove(target);
    }

    private IJavaClassType loadRemoteAgent(IJavaDebugTarget target, IJavaThread thread) throws VariableTransferException {
        FutureTask<IJavaClassType> future = new FutureTask<>(new LoadAgentClasses(target, thread, MirurAgent.class.getName()));
        thread.queueRunnable(future);

        try {
            return future.get();
        } catch (InterruptedException ex) {
            throw new VariableTransferException(ex);
        } catch (ExecutionException ex) {
            throw new VariableTransferException(VariableTransferException.ERR_Exception_in_Agent_Install, ex.getCause());
        }
    }

    private File explodeAgentClasses() throws IOException {
        File tmpClasspathDir = Files.createTempDirectory("miruragent").toFile();
        logFine(LOGGER, "Writing agent class files to %s", tmpClasspathDir);

        for (String className : MirurAgent.AGENT_CLASSES) {
            String asFile = className.replace('.', '/').concat(".class");
            File classFile = new File(tmpClasspathDir, asFile);
            File classFileParent = classFile.getParentFile();
            if (!classFileParent.exists()) {
                classFileParent.mkdirs();
            }

            logFine(LOGGER, "Writing agent class %s file to %s", className, classFile);
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
        logFine(LOGGER, "Loading the agent using the isolated ClassLoader");

        // java.net.URLClassLoader
        IJavaType[] types = target.getJavaTypes(URLClassLoader.class.getName());
        if (types == null) {
            // possible in Android
            throw new VariableTransferException(VariableTransferException.ERR_URLClassLoader_Not_Found);
        }
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

        logFiner(LOGGER, "Retrieve all necessary types successfully");

        // create the URL object we need
        // new URL(String)
        IJavaValue classNameString = target.newValue(classpathDir.toURI().toURL().toString());
        IJavaValue[] args = new IJavaValue[] { classNameString };
        // IJavaObject urlObject = (IJavaObject) urlType.sendMessage("<init>", "(Ljava/lang/String;)V", args, thread);
        IJavaObject urlObject = urlType.newInstance("(Ljava/lang/String;)V", args, thread);
        logFiner(LOGGER, "Called URL.<init>(String) successfully");

        // create the URL[] array
        // Array.newInstance(Class,int)
        args = new IJavaValue[] { urlType.getClassObject(), target.newValue(1) };
        IJavaValue urlArrayObject = arrayType.sendMessage("newInstance", "(Ljava/lang/Class;I)Ljava/lang/Object;", args, thread);
        logFiner(LOGGER, "Called Array.newInstance(Class, int) successfully");

        // set the value
        // Array.set(Object,int,Object)
        args = new IJavaValue[] { urlArrayObject, target.newValue(0), urlObject };
        arrayType.sendMessage("set", "(Ljava/lang/Object;ILjava/lang/Object;)V", args, thread);
        logFiner(LOGGER, "Called Array.set(Object, int, Object) successfully");

        // create the URLClassLoader
        // URLClassLoader.newInstance(URL[])
        args = new IJavaValue[] { urlArrayObject };
        IJavaValue classLoaderObject = classloaderType.sendMessage("newInstance", "([Ljava/net/URL;)Ljava/net/URLClassLoader;", args, thread);
        logFiner(LOGGER, "Called URLClassLoader.newInstance(URL[]) successfully");

        // load the class
        // Class.forName(String,boolean,ClassLoader)
        args = new IJavaValue[] { target.newValue(agentClassName), target.newValue(true), classLoaderObject };
        IJavaValue classObject = classType.sendMessage("forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;", args, thread);
        logFiner(LOGGER, "Called Class.forName(String, ClassLoader) successfully");

        return (IJavaClassType) ((IJavaClassObject) classObject).getInstanceType();
    }

    private class LoadAgentClasses implements Callable<IJavaClassType> {
        final IJavaDebugTarget target;
        final IJavaThread thread;
        final String agentClassName;

        LoadAgentClasses(IJavaDebugTarget target, IJavaThread thread, String agentClassName) {
            this.target = target;
            this.thread = thread;
            this.agentClassName = agentClassName;
        }

        @Override
        public IJavaClassType call() throws Exception {
            return loadAgentClass(target, thread, agentClassesDir, agentClassName);
        }
    }
}

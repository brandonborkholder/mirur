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

        // Earlier than Java 5, won't work
        if (isJVMEarlierThan5(target.getVersion())) {
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

        Callable<IJavaClassType> loaderFn;
        // if (isJVMEarlierThan9(target.getVersion())) {
        // Earlier than Java 9, use simple URLClassLoader
        loaderFn = () -> loadAgentClasses(target, thread, agentClassesDir, MirurAgent.class.getName());
        // } else {
        // // Use Java 9+ modules
        // loaderFn = () -> loadAgentModule(target, thread, agentClassesDir, MirurAgent.class.getName());
        // }

        IJavaClassType agentType = loadRemoteAgent(target, thread, loaderFn);
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

    private IJavaClassType loadRemoteAgent(IJavaDebugTarget target, IJavaThread thread, Callable<IJavaClassType> loaderFn) throws VariableTransferException {
        FutureTask<IJavaClassType> future = new FutureTask<>(loaderFn);
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

        tmpClasspathDir.deleteOnExit();
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

    private boolean isJVMEarlierThan5(String version) {
        int secondDot = version.indexOf('.');
        secondDot = version.indexOf('.', secondDot + 1);
        String major = version.substring(0, secondDot);

        return Double.parseDouble(major) < 1.5;
    }

    private boolean isJVMEarlierThan9(String version) {
        int secondDot = version.indexOf('.');
        secondDot = version.indexOf('.', secondDot + 1);
        String major = version.substring(0, secondDot);

        return Double.parseDouble(major) < 9;
    }

    private IJavaClassType loadAgentClasses(IJavaDebugTarget target, IJavaThread thread, File classpathDir, String agentClassName)
            throws DebugException, MalformedURLException {
        logFine(LOGGER, "Loading the agent using the isolated ClassLoader");

        // Force the JVM to load URLClassLoader if not already loaded
        {
            IJavaClassType classType = (IJavaClassType) target.getJavaTypes("java.lang.Class")[0];
            IJavaValue[] args = new IJavaValue[] { target.newValue(URLClassLoader.class.getName()) };
            classType.sendMessage("forName", "(Ljava/lang/String;)Ljava/lang/Class;", args, thread);
        }

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

    /**
     * <pre>
     * Path path = Paths.get("/path/to/files");
     * ModuleFinder finder = ModuleFinder.of(path);
     * ModuleLayer parent = ModuleLayer.boot();
     * Configuration cf = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("mirur.core.agent"));
     * ClassLoader scl = ClassLoader.getSystemClassLoader();
     * ModuleLayer layer = parent.defineModulesWithOneLoader(cf, scl);
     * Class<?> c = layer.findLoader("mirur.core.agent").loadClass("mirur.agent");
     * </pre>
     */
    private IJavaClassType loadAgentModule(IJavaDebugTarget target, IJavaThread thread, File classpathDir, String agentClassName) throws DebugException {
        IJavaClassType pathsType = (IJavaClassType) target.getJavaTypes("java.nio.file.Paths")[0];
        IJavaClassType moduleFinderType = (IJavaClassType) target.getJavaTypes("java.lang.ModuleFinder")[0];
        IJavaClassType moduleLayerType = (IJavaClassType) target.getJavaTypes("java.lang.ModuleLayer")[0];
        IJavaClassType setType = (IJavaClassType) target.getJavaTypes("java.util.Set")[0];
        IJavaClassType configurationType = (IJavaClassType) target.getJavaTypes("java.lang.module.Configuration")[0];
        IJavaClassType classLoaderType = (IJavaClassType) target.getJavaTypes("java.lang.ClassLoader")[0];

        // Path path = Paths.get("/path/to/files");
        IJavaValue[] args = new IJavaValue[] { target.newValue(classpathDir.getAbsolutePath()) };
        IJavaValue path = pathsType.sendMessage("get", "(Ljava/lang/String;)Ljava/nio/file/Path;", args, thread);

        // ModuleFinder finder = ModuleFinder.of(path);
        args = new IJavaValue[] { path };
        IJavaValue finder = moduleFinderType.sendMessage("of", "(Ljava/nio/file/Path;)Ljava/lang/ModuleFinder;", args, thread);

        // ModuleLayer parent = ModuleLayer.boot();
        args = new IJavaValue[] {};
        IJavaValue parent = moduleLayerType.sendMessage("boot", "()Ljava/lang/ModuleLayer;", args, thread);

        // Configuration cf = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("mirur.core.agent"));
        args = new IJavaValue[] { parent };
        IJavaValue configuration = moduleLayerType.sendMessage("configuration", "()Ljava/lang/module/Configuration;", args, thread);
        args = new IJavaValue[] {};
        IJavaValue emptyModuleFinder = moduleFinderType.sendMessage("of", "()Ljava/lang/ModuleFinder;", args, thread);
        args = new IJavaValue[] { target.newValue("mirur.core.agent") };
        IJavaValue agentModuleNameSet = setType.sendMessage("of", "(Ljava/lang/String;)Ljava/util/Set;", args, thread);
        args = new IJavaValue[] { configuration, finder, emptyModuleFinder, agentModuleNameSet };
        IJavaValue cf = configurationType.sendMessage("resolve", "(Ljava/lang/module/Configuration;Ljava/lang/ModuleFinder;Ljava/util/Collection;)Ljava/lang/module/Configuration;", args, thread);

        // ClassLoader scl = ClassLoader.getSystemClassLoader();
        args = new IJavaValue[] {};
        IJavaValue scl = classLoaderType.sendMessage("getSystemClassLoader", "()V", args, thread);

        // ModuleLayer layer = parent.defineModulesWithOneLoader(cf, scl);
        args = new IJavaValue[] { parent, cf, scl };
        IJavaValue layer = moduleLayerType.sendMessage("defineModulesWithOneLoader", "(Ljava/lang/ModuleLayer;Ljava/lang/module/Configuration;Ljava/lang/ClassLoader;)Ljava/lang/ModuleLayer;", args, thread);

        // Class<?> c = layer.findLoader("mirur.core.agent").loadClass("mirur.agent");
        args = new IJavaValue[] { layer, target.newValue("mirur.core.agent") };
        IJavaValue loader = moduleLayerType.sendMessage("findLoader", "(Ljava/lang/String;)Ljava/lang/ClassLoader;", args, thread);
        args = new IJavaValue[] { loader, target.newValue(agentClassName) };
        IJavaValue c = classLoaderType.sendMessage("loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", args, thread);

        return (IJavaClassType) ((IJavaClassObject) c).getInstanceType();
    }
}

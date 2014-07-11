package mirur.plugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mirur.core.MirurAgent;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

public class RemoteAgentDeployer {
    private byte[] agentClassBytes;

    public void install(IJavaProject project) {
        try {
            File classFileDest = getAgentDestination(project);
            writeAgent(classFileDest);
        } catch (JavaModelException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeAgent(File classFileDest) throws IOException {
        if (agentClassBytes == null) {
            agentClassBytes = loadMirurAgentClassBytes();
        }

        File parent = classFileDest.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        try (OutputStream out = new FileOutputStream(classFileDest)) {
            out.write(agentClassBytes);
        }
    }

    private byte[] loadMirurAgentClassBytes() throws IOException {
        String simpleClassName = MirurAgent.class.getSimpleName() + ".class";
        try (InputStream in = MirurAgent.class.getResourceAsStream(simpleClassName)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
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
            return out.toByteArray();
        }
    }

    private File getAgentDestination(IJavaProject project) throws JavaModelException {
        File outputDir = getClassesDirectory(project);
        String packagePath = MirurAgent.class.getPackage().getName().replace(".", "/");
        File directory = new File(outputDir, packagePath);

        String fileName = MirurAgent.class.getSimpleName() + ".class";
        File classFile = new File(directory, fileName);

        return classFile;
    }

    private File getClassesDirectory(IJavaProject project) throws JavaModelException {
        final IClasspathEntry[] rawClasspath = project.getRawClasspath();
        for (IClasspathEntry e : rawClasspath) {
            if (e.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                IPath path = e.getOutputLocation();
                if (path == null) {
                    path = project.getOutputLocation();
                }

                IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
                return file.getRawLocation().toFile();
            }
        }

        return null;
    }

    public boolean isInstalled(IJavaProject project) {
        try {
            File classFileDest = getAgentDestination(project);
            return classFileDest.isFile();
        } catch (JavaModelException ex) {
            return false;
        }
    }
}

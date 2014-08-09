package mirur.plugin.statsview;

import java.io.PrintStream;

import mirur.core.AbstractArrayVisitor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

public class Array2dToCsvVisitor extends AbstractArrayVisitor {
    private final PrintStream out;
    private final IProgressMonitor monitor;
    private final String taskName;

    public Array2dToCsvVisitor(PrintStream out, String taskName, IProgressMonitor monitor) {
        this.out = out;
        this.monitor = monitor;
        this.taskName = taskName;
    }

    private void incr() {
        monitor.worked(1);
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    @Override
    public void visit(double[][] v) {
        monitor.beginTask(taskName, v.length);
        super.visit(v);
    }

    @Override
    public void visit(float[][] v) {
        monitor.beginTask(taskName, v.length);
        super.visit(v);
    }

    @Override
    public void visit(long[][] v) {
        monitor.beginTask(taskName, v.length);
        super.visit(v);
    }

    @Override
    public void visit(int[][] v) {
        monitor.beginTask(taskName, v.length);
        super.visit(v);
    }

    @Override
    public void visit(short[][] v) {
        monitor.beginTask(taskName, v.length);
        super.visit(v);
    }

    @Override
    public void visit(char[][] v) {
        monitor.beginTask(taskName, v.length);
        super.visit(v);
    }

    @Override
    public void visit(byte[][] v) {
        monitor.beginTask(taskName, v.length);
        super.visit(v);
    }

    @Override
    public void visit(boolean[][] v) {
        monitor.beginTask(taskName, v.length);
        super.visit(v);
    }

    @Override
    public void visit(int i, double[] v) {
        super.visit(i, v);
        out.println();
        incr();
    }

    @Override
    public void visit(int i, float[] v) {
        super.visit(i, v);
        out.println();
        incr();
    }

    @Override
    public void visit(int i, long[] v) {
        super.visit(i, v);
        out.println();
        incr();
    }

    @Override
    public void visit(int i, int[] v) {
        super.visit(i, v);
        out.println();
        incr();
    }

    @Override
    public void visit(int i, short[] v) {
        super.visit(i, v);
        out.println();
        incr();
    }

    @Override
    public void visit(int i, char[] v) {
        super.visit(i, v);
        out.println();
        incr();
    }

    @Override
    public void visit(int i, byte[] v) {
        super.visit(i, v);
        out.println();
        incr();
    }

    @Override
    public void visit(int i, boolean[] v) {
        super.visit(i, v);
        out.println();
        incr();
    }

    @Override
    public void visit(int i, int j, double v) {
        if (j > 0) {
            out.print(",");
        }

        out.print(v);
    }

    @Override
    public void visit(int i, int j, long v) {
        if (j > 0) {
            out.print(",");
        }

        out.print(v);
    }

    @Override
    public void visit(int i, int j, float v) {
        if (j > 0) {
            out.print(",");
        }

        out.print(v);
    }

    @Override
    public void visit(int i, int j, int v) {
        if (j > 0) {
            out.print(",");
        }

        out.print(v);
    }

    @Override
    public void visit(int i, int j, short v) {
        if (j > 0) {
            out.print(",");
        }

        out.print(v);
    }

    @Override
    public void visit(int i, int j, char v) {
        if (j > 0) {
            out.print(",");
        }

        out.print((int) v);
    }

    @Override
    public void visit(int i, int j, byte v) {
        if (j > 0) {
            out.print(",");
        }

        out.print(v);
    }

    @Override
    public void visit(int i, int j, boolean v) {
        if (j > 0) {
            out.print(",");
        }

        out.print(v);
    }
}

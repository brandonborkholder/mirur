/*
 * Mirur - Visually debug arrays in your IDE.
 * Copyright © 2023 Brandon Borkholder (support@mirur.io)
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
package mirur.plugin.statsview;

import java.io.PrintStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import mirur.core.Array1dVisitor;

public class Array1dToCsvVisitor implements Array1dVisitor {
    private final PrintStream out;
    private final IProgressMonitor monitor;
    private final String taskName;

    public Array1dToCsvVisitor(PrintStream out, String taskName, IProgressMonitor monitor) {
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
    public void visit(double[] v) {
        monitor.beginTask(taskName, v.length);
        for (int i = 0; i < v.length; i++) {
            out.printf("%.18g", v[i]);
            out.println();
            incr();
        }
    }

    @Override
    public void visit(float[] v) {
        monitor.beginTask(taskName, v.length);
        for (int i = 0; i < v.length; i++) {
            out.printf("%.9g", v[i]);
            out.println();
            incr();
        }
    }

    @Override
    public void visit(long[] v) {
        monitor.beginTask(taskName, v.length);
        for (int i = 0; i < v.length; i++) {
            out.print(v[i]);
            out.println();
            incr();
        }
    }

    @Override
    public void visit(int[] v) {
        monitor.beginTask(taskName, v.length);
        for (int i = 0; i < v.length; i++) {
            out.print(v[i]);
            out.println();
            incr();
        }
    }

    @Override
    public void visit(short[] v) {
        monitor.beginTask(taskName, v.length);
        for (int i = 0; i < v.length; i++) {
            out.print(v[i]);
            out.println();
            incr();
        }
    }

    @Override
    public void visit(char[] v) {
        monitor.beginTask(taskName, v.length);
        for (int i = 0; i < v.length; i++) {
            // since we represent it as numeric all over, let's show as numbers
            out.printf("%x", v[i]);
            out.println();
            incr();
        }
    }

    @Override
    public void visit(byte[] v) {
        monitor.beginTask(taskName, v.length);
        for (int i = 0; i < v.length; i++) {
            out.printf("%x", v[i]);
            out.println();
            incr();
        }
    }

    @Override
    public void visit(boolean[] v) {
        monitor.beginTask(taskName, v.length);
        for (int i = 0; i < v.length; i++) {
            out.print(v[i] ? 1 : 0);
            out.println();
            incr();
        }
    }
}

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
package mirur.plugin.statsview;

import java.util.ArrayList;
import java.util.List;

import mirur.core.PrimitiveArray;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public abstract class StatsComputeJob extends Job {
    private final PrimitiveArray array;
    private final List<ArrayStatisticVisitor> statistics;

    public StatsComputeJob(PrimitiveArray array, List<ArrayStatisticVisitor> statistics) {
        super("Calculating Array Statistics");
        this.array = array;
        this.statistics = statistics;

        setPriority(Job.SHORT);
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        List<Job> jobs = new ArrayList<>(statistics.size());

        monitor.beginTask(getName(), statistics.size());
        for (int i = 0; i < statistics.size(); i++) {
            final int index = i;
            Job job = new StatisticComputeJob(array, statistics.get(i)) {
                @Override
                protected void finished(String statValue) {
                    update(index, statValue);
                    monitor.worked(1);
                }
            };
            job.schedule();
            jobs.add(job);
        }

        try {
            for (Job job : jobs) {
                job.join();
            }
        } catch (InterruptedException ex) {
            // ignore
        }

        monitor.done();

        return Status.OK_STATUS;
    }

    protected abstract void update(int index, String value);
}
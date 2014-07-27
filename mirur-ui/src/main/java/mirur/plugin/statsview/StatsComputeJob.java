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
    protected IStatus run(IProgressMonitor monitor) {
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
            // TODO how to handle this
            throw new RuntimeException(ex);
        }

        monitor.done();

        return Status.OK_STATUS;
    }

    protected abstract void update(int index, String value);
}
package mirur.plugin.statsview;

import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public abstract class StatisticComputeJob extends Job {
    private final PrimitiveArray array;
    private final ArrayStatisticVisitor statistic;

    public StatisticComputeJob(PrimitiveArray array, ArrayStatisticVisitor statistic) {
        super("Calculating " + statistic.getName() + " for " + array.getName());
        this.array = array;
        this.statistic = statistic;

        setPriority(Job.SHORT);
        setSystem(true);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        VisitArray.visit(array.getData(), statistic);

        String value = statistic.getStatistic();
        finished(value);

        return Status.OK_STATUS;
    }

    protected abstract void finished(String statValue);
}
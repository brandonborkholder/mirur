package mirur.plugin.statsview;

import java.util.ArrayList;
import java.util.List;

import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

public abstract class StatsComputeJob extends Job {
    private final PrimitiveArray array;
    private final List<ArrayStatisticVisitor> statistics;

    public StatsComputeJob(PrimitiveArray array, List<ArrayStatisticVisitor> statistics) {
        super("Calculate Array Statistics");
        this.array = array;
        this.statistics = statistics;

        setPriority(Job.SHORT);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        List<String[]> keyValues = new ArrayList<>();
        for (ArrayStatisticVisitor visitor : statistics) {
            VisitArray.visit(array.getData(), visitor);
            keyValues.add(new String[] { visitor.getName(), visitor.getStatistic() });
        }

        final String[][] data = keyValues.toArray(new String[0][]);
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                finished(data);
            }
        });

        return Status.OK_STATUS;
    }

    protected abstract void finished(String[][] data);
}
package mirur.plugin.statsview;

import static mirur.core.PrimitiveTest.getPrimitiveComponent;
import static mirur.plugin.Model.MODEL;

import java.util.ArrayList;
import java.util.List;

import mirur.core.Array1D;
import mirur.core.Array2D;
import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;
import mirur.plugin.ArraySelectListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

public class ArrayStatsView extends ViewPart implements ArraySelectListener {
    private PrimitiveArray currentData;

    private TableViewer table;

    @Override
    public void createPartControl(Composite parent) {
        table = new TableViewer(parent, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
        table.getTable().setHeaderVisible(true);
        table.getTable().setLinesVisible(true);

        table.setContentProvider(ArrayContentProvider.getInstance());
        TableViewerColumn keyColumn = new TableViewerColumn(table, SWT.NONE);
        keyColumn.getColumn().setText("Name");
        keyColumn.getColumn().setWidth(200);
        keyColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((String[]) element)[0];
            }
        });
        TableViewerColumn valueColumn = new TableViewerColumn(table, SWT.NONE);
        valueColumn.getColumn().setText("Value");
        valueColumn.getColumn().setWidth(200);
        valueColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((String[]) element)[1];
            }
        });

        MODEL.addArrayListener(this, this);
    }

    @Override
    public void setFocus() {
        table.getTable().setFocus();
    }

    @Override
    public void dispose() {
        MODEL.removeArrayListener(this, this);
        super.dispose();
    }

    @Override
    public void arraySelected(PrimitiveArray array) {
        currentData = array;
        refreshData();
    }

    private void refreshData() {
        Object data = null;
        if (currentData instanceof Array1D || currentData instanceof Array2D) {
            data = currentData.getData();
        } else {
            table.setInput(null);
            table.refresh();
            return;
        }

        List<ArrayStatisticVisitor> statsVisitors = getStats(data.getClass());
        new StatsComputeJob(data, statsVisitors).schedule();
    }

    private List<ArrayStatisticVisitor> getStats(Class<?> clazz) {
        List<ArrayStatisticVisitor> stats = new ArrayList<>();

        String primitive = getPrimitiveComponent(clazz).getSimpleName();
        switch (primitive) {
        case "double":
        case "float":
            stats.add(new ArrayStatisticVisitor.Min());
            stats.add(new ArrayStatisticVisitor.Max());
            stats.add(new ArrayStatisticVisitor.Sum());
            stats.add(new ArrayStatisticVisitor.Mean());
            stats.add(new ArrayStatisticVisitor.Variance());
            stats.add(new ArrayStatisticVisitor.CountNegInf());
            stats.add(new ArrayStatisticVisitor.CountPosInf());
            stats.add(new ArrayStatisticVisitor.CountNaN());
            stats.add(new ArrayStatisticVisitor.CountNegative());
            stats.add(new ArrayStatisticVisitor.CountPositive());
            stats.add(new ArrayStatisticVisitor.CountZero());
            break;

        case "long":
        case "int":
        case "short":
        case "byte":
        case "char":
            stats.add(new ArrayStatisticVisitor.Min());
            stats.add(new ArrayStatisticVisitor.Max());
            stats.add(new ArrayStatisticVisitor.SumLong());
            stats.add(new ArrayStatisticVisitor.Mean());
            stats.add(new ArrayStatisticVisitor.Variance());
            stats.add(new ArrayStatisticVisitor.CountNegative());
            stats.add(new ArrayStatisticVisitor.CountPositive());
            stats.add(new ArrayStatisticVisitor.CountZero());
            break;

        case "boolean":
            stats.add(new ArrayStatisticVisitor.CountTrue());
            stats.add(new ArrayStatisticVisitor.CountFalse());
            break;

        default:
            throw new AssertionError(clazz.getSimpleName());
        }

        return stats;
    }

    private void updateTable(String[][] data) {
        table.setInput(data);
        table.refresh();
    }

    private class StatsComputeJob extends Job {
        final Object array;
        final List<ArrayStatisticVisitor> statistics;

        public StatsComputeJob(Object array, List<ArrayStatisticVisitor> statistics) {
            super("Calculate Array Statistics");
            this.array = array;
            this.statistics = statistics;

            setPriority(Job.SHORT);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            List<String[]> keyValues = new ArrayList<>();
            for (ArrayStatisticVisitor visitor : statistics) {
                VisitArray.visit(array, visitor);
                keyValues.add(new String[] { visitor.getName(), visitor.getStatistic() });
            }

            final String[][] data = keyValues.toArray(new String[0][]);
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    updateTable(data);
                }
            });

            return Status.OK_STATUS;
        }
    }
}

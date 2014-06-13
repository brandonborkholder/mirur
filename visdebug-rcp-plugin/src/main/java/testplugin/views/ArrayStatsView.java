package testplugin.views;

import static testplugin.views.Model.MODEL;

import java.util.ArrayList;
import java.util.List;

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

import test_plugin.Activator;

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

        Activator.getDefault().initVariableSelectListener(this);
        MODEL.addArrayListener(this);
    }

    @Override
    public void setFocus() {
        table.getTable().setFocus();
    }

    @Override
    public void dispose() {
        MODEL.removeArrayListener(this);
        super.dispose();
    }

    @Override
    public void selected(PrimitiveArray array) {
        currentData = array;
        refreshData();
    }

    private void refreshData() {
        Object data = null;
        if (currentData instanceof Array1D) {
            data = currentData.getData();
        } else {
            table.setInput(null);
            table.refresh();
            return;
        }

        final List<ArrayStatisticVisitor> statsVisitors = new ArrayList<>();
        statsVisitors.add(new ArrayStatisticVisitor.Min());
        statsVisitors.add(new ArrayStatisticVisitor.Max());
        statsVisitors.add(new ArrayStatisticVisitor.Sum());
        statsVisitors.add(new ArrayStatisticVisitor.Mean());
        statsVisitors.add(new ArrayStatisticVisitor.CountNaN());
        statsVisitors.add(new ArrayStatisticVisitor.CountNegInf());
        statsVisitors.add(new ArrayStatisticVisitor.CountPosInf());

        new StatsComputeJob(data, statsVisitors).schedule();
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

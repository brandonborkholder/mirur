package mirur.plugin.statsview;

import static mirur.core.PrimitiveTest.getPrimitiveComponent;

import java.util.ArrayList;
import java.util.List;

import mirur.core.Array1D;
import mirur.core.Array2D;
import mirur.core.PrimitiveArray;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;

public class StatisticsPage extends Page {
    private TableViewer table;

    @Override
    public void createControl(Composite parent) {
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
    }

    @Override
    public Control getControl() {
        return table.getControl();
    }

    @Override
    public void setFocus() {
        table.getTable().setFocus();
    }

    public void setInput(PrimitiveArray array) {
        if (array instanceof Array1D || array instanceof Array2D) {
            List<ArrayStatisticVisitor> statsVisitors = getStats(array);
            computeStats(array, statsVisitors);
        } else {
            updateTable(null);
        }
    }

    private void computeStats(PrimitiveArray array, List<ArrayStatisticVisitor> statsVisitors) {
        new StatsComputeJob(array, statsVisitors) {
            @Override
            protected void finished(String[][] data) {
                updateTable(data);
            }
        }.schedule();
    }

    private void updateTable(String[][] data) {
        table.setInput(data);
        table.refresh();
    }

    private List<ArrayStatisticVisitor> getStats(PrimitiveArray array) {
        List<ArrayStatisticVisitor> stats = new ArrayList<>();

        String primitive = getPrimitiveComponent(array.getData().getClass()).getSimpleName();
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
            throw new AssertionError(array.getData().getClass().getSimpleName());
        }

        return stats;
    }

}
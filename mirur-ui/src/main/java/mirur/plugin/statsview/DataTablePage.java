package mirur.plugin.statsview;

import mirur.core.Array1D;
import mirur.core.ElementToStringVisitor;
import mirur.core.PrimitiveArray;
import mirur.core.VisitArray;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.Page;

public class DataTablePage extends Page {
    private Table table;
    private Object array;

    @Override
    public void createControl(Composite parent) {
        table = new Table(parent, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.VIRTUAL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn indexCol = new TableColumn(table, SWT.NONE);
        indexCol.setText("Index");
        indexCol.setWidth(50);
        TableColumn valueCol = new TableColumn(table, SWT.NONE);
        valueCol.setText("Value");
        valueCol.setWidth(200);

        table.setItemCount(0);
        table.addListener(SWT.SetData, new Listener() {
            @Override
            public void handleEvent(Event event) {
                TableItem item = (TableItem) event.item;
                int index = event.index;
                item.setText(0, String.valueOf(index));
                item.setText(1, VisitArray.visit(array, new ElementToStringVisitor(), index).getText());
            }
        });
    }

    public void setInput(PrimitiveArray array) {
        if (array instanceof Array1D) {
            this.array = array.getData();
            table.setItemCount(array.getSize(0));
        } else {
            this.array = null;
            table.setItemCount(0);
        }
        table.clearAll();
    }

    @Override
    public Control getControl() {
        return table;
    }

    @Override
    public void setFocus() {
        table.setFocus();
    }
}
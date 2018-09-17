package mirur.plugin.detailpane;

import static org.eclipse.swt.layout.GridData.FILL_BOTH;
import static org.eclipse.swt.layout.GridData.FILL_HORIZONTAL;

import org.eclipse.debug.ui.IDetailPane;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPartSite;

public class ArrayDetailPane implements IDetailPane {
    public static final String ID = "mirur.detailpanes.simplearray.temp";
    public static final String NAME = "Simple Array Details";
    public static final String DESCRIPTION = "Only print the first several values of the array to avoid very long evaluation times.";

    private Composite container;
    private Label label;

    @Override
    public void init(IWorkbenchPartSite partSite) {
        // nop
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void dispose() {
        container.dispose();
    }

    @Override
    public Control createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        container.setFont(parent.getFont());
        GridData gd = new GridData(FILL_BOTH);
        gd.horizontalSpan = 1;
        container.setLayoutData(gd);
        container.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

        label = new Label(container, SWT.WRAP);
        label.setFont(container.getFont());
        gd = new GridData(FILL_HORIZONTAL);
        gd.horizontalSpan = 1;
        label.setLayoutData(gd);

        return container;
    }

    @Override
    public boolean setFocus() {
        return false;
    }

    @Override
    public void display(IStructuredSelection selection) {
        // re-create controls if the layout has changed
        if (selection != null && selection.size() == 1) {
            Object input = selection.getFirstElement();
            if (input instanceof String) {
                String message = (String) input;
                label.setText(message);
                container.layout(true);
            } else {
                label.setText(input.getClass().getName());
                container.layout(true);
            }
        } else if (selection == null || selection.isEmpty()) {
            label.setText("");
            container.layout(true);
        }
    }
}

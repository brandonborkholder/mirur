package mirur.plugin.statsview;

import java.io.File;

import mirur.core.PrimitiveArray;
import mirur.plugin.Activator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class SaveArrayToFileAction extends Action {
    public SaveArrayToFileAction() {
        super("Save Data", IAction.AS_PUSH_BUTTON);
        setId(SaveArrayToFileAction.class.getName());
        setToolTipText("Save Array Values to File");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
    }

    @Override
    public void run() {
        PrimitiveArray array = Activator.getSelectionModel().getActiveSelected();
        if (array == null) {
            return;
        }

        FileDialog saveDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
        saveDialog.setFileName(array.getName() + ".csv");
        String destination = saveDialog.open();
        if (destination != null) {
            File fileDest = new File(destination);
            new SaveArrayToFileJob(array, fileDest).schedule();
        }
    }
}

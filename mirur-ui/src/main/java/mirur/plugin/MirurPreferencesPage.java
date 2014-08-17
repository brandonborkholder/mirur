package mirur.plugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class MirurPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    public static final String ID = "mirur.preferences.main";

    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(Preferences.PREF_SUBMIT_STATISTICS, "Submit anonymous &usage statistics?", getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Settings and configuration for the Mirur Plugin");
    }
}

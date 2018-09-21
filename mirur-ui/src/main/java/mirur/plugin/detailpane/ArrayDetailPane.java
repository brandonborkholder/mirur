/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugin.detailpane;

import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.IDebugHelpContextIds;
import org.eclipse.debug.internal.ui.preferences.IDebugPreferenceConstants;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.IDetailPane;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class ArrayDetailPane implements IDetailPane {
    public static final String ID = "mirur.detailpanes.simplearray";
    public static final String NAME = "Simple Array Details";
    public static final String DESCRIPTION = "Only print the first several values of the array to avoid very long evaluation times.";

    private Control container;
    private SourceViewer sourceViewer;

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
        sourceViewer = new SourceViewer(parent, null, SWT.V_SCROLL | SWT.H_SCROLL);
        sourceViewer.setDocument(new Document());
        sourceViewer.getTextWidget().setFont(JFaceResources.getFont(IDebugUIConstants.PREF_DETAIL_PANE_FONT));
        sourceViewer.getTextWidget()
                .setWordWrap(DebugUIPlugin.getDefault().getPreferenceStore().getBoolean(IDebugPreferenceConstants.PREF_DETAIL_PANE_WORD_WRAP));
        sourceViewer.setEditable(false);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(sourceViewer.getTextWidget(), IDebugHelpContextIds.DETAIL_PANE);
        container = sourceViewer.getControl();
        GridData gd = new GridData(GridData.FILL_BOTH);
        container.setLayoutData(gd);
        return container;
    }

    @Override
    public boolean setFocus() {
        return sourceViewer.getTextWidget().setFocus();
    }

    @Override
    public void display(IStructuredSelection selection) {
        String text = ArrayDetailPaneFactory.toString(selection);
        sourceViewer.getDocument().set(text);
    }
}

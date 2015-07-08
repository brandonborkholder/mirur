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
package mirur.plugin.painterview;

import static com.metsci.glimpse.util.logging.LoggerUtils.logFine;
import static com.metsci.glimpse.util.logging.LoggerUtils.logInfo;
import static com.metsci.glimpse.util.logging.LoggerUtils.logSevere;
import static org.apache.commons.lang3.StringEscapeUtils.escapeJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import mirur.plugin.Activator;
import mirur.plugin.Icons;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class RequestNewViewAction extends Action {
    private static final Logger LOGGER = Logger.getLogger(RequestNewViewAction.class.getName());

    private static final String TEMPLATE;

    static {
        StringBuilder builder = new StringBuilder();

        builder.append("Source Data:");
        builder.append("\n");
        builder.append("type and format");
        builder.append("\n");
        builder.append("e.g. float[] where each value is an angle in degrees");
        builder.append("\n");
        builder.append("\n");
        builder.append("View:");
        builder.append("\n");
        builder.append("description of how to view the data, along with any options");
        builder.append("\n");
        builder.append("e.g. a polar plot with a constant radius");
        builder.append("\n");
        builder.append("     each value is a line along the given angle");
        builder.append("\n");
        builder.append("     I'd like to be able to show each line in a different color or all the same");
        builder.append("\n");
        builder.append("\n");
        builder.append("Followup Information:");
        builder.append("\n");
        builder.append("your name and email if you'd like us to follow up with you");
        builder.append("\n");

        TEMPLATE = builder.toString();
    }

    public RequestNewViewAction() {
        setId(RequestNewViewAction.class.getName());
        setText("Request New View ...");
        setToolTipText("Submit an idea for a new view on your data");
    }

    @Override
    public void runWithEvent(Event event) {
        ScrollableDialog dialog = new ScrollableDialog(event.display.getActiveShell(), TEMPLATE);
        dialog.setBlockOnOpen(true);
        dialog.open();
    }

    /*
     * From
     * https://stackoverflow.com/questions/3180016/eclipse-rcp-dialog-large-text
     * -box-wont-scroll-instead-creates-a-huge-dialog
     */
    private static class ScrollableDialog extends TitleAreaDialog {
        private Text textArea;
        private String scrollableText;

        public ScrollableDialog(Shell parentShell, String scrollableText) {
            super(parentShell);
            this.scrollableText = scrollableText;
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite) super.createDialogArea(parent);

            GridData gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            gridData.grabExcessVerticalSpace = true; // Layout vertically, too!
            gridData.verticalAlignment = GridData.FILL;

            textArea = new Text(composite, SWT.BORDER | SWT.V_SCROLL);
            textArea.setEditable(true);
            textArea.setLayoutData(gridData);
            textArea.setText(scrollableText);

            setMessage("Enter the details below to request a new view of your data");
            setTitleImage(Activator.getCachedImage(Icons.MIRUR_64_PATH));
            setTitle("Submit Idea for a New View");

            return composite;
        }

        @Override
        protected void createButtonsForButtonBar(Composite parent) {
            createButton(parent, OK, "Submit", true);
            createButton(parent, CANCEL, "Cancel", false);
        }

        @Override
        protected void buttonPressed(int buttonId) {
            if (buttonId == OK) {
                String text = textArea.getText();
                new SubmitNewViewIdeaJob(text).schedule();
            }

            super.buttonPressed(buttonId);
        }

        @Override
        protected boolean isResizable() {
            return true; // Allow the user to change the dialog size!
        }
    }

    private static class SubmitNewViewIdeaJob extends Job {
        private final String text;

        public SubmitNewViewIdeaJob(String text) {
            super("Submitting New View Idea");
            this.text = text;

            setPriority(SHORT);
            setUser(true);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            String url = "https://api.emailyak.com/v1/1ujk9wvvcuz2m83/json/send/email/";
            try {
                URLConnection connection = new URL(url).openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                try (OutputStream output = connection.getOutputStream()) {
                    logInfo(LOGGER, "Submitting request for %s", text);
                    String postBody = buildBody();

                    logFine(LOGGER, "Submitting email request %s", postBody);
                    output.write(postBody.getBytes());
                }

                try (InputStream response = connection.getInputStream()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                    StringBuilder responseBody = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        responseBody.append(line);
                    }

                    logFine(LOGGER, "Received response %s", responseBody);
                }

                return Status.OK_STATUS;
            } catch (IOException ex) {
                logSevere(LOGGER, "Error submitting feedback", ex);
                // I don't log the error because it may have the URL with the
                // api key in the exception
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error submitting feedback");
            }
        }

        private String buildBody() {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            builder.append("\n");
            builder.append("\"FromName\": ");
            builder.append("\"mirur-plugin-request\",");
            builder.append("\n");
            builder.append("\"FromAddress\": ");
            builder.append("\"mirur-request@mirur.simpleyak.com\",");
            builder.append("\n");
            builder.append("\"ToAddress\": ");
            builder.append("\"plugin-dropbox@mirur.io\",");
            builder.append("\n");
            builder.append("\"TextBody\": ");
            builder.append("\"");
            builder.append(escapeJson(text));
            builder.append("\",");
            builder.append("\n");
            builder.append("\"Subject\": ");
            builder.append("\"mirur request new view\"");
            builder.append("\n");
            builder.append("}");

            return builder.toString();
        }
    }
}
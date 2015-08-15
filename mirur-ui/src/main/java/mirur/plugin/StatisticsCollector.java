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
package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logFine;
import static com.metsci.glimpse.util.logging.LoggerUtils.logInfo;
import static com.metsci.glimpse.util.logging.LoggerUtils.logSevere;
import static mirur.plugin.Activator.getPreferences;
import static org.apache.commons.lang3.StringEscapeUtils.escapeJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import mirur.core.VariableObject;
import mirur.plugins.MirurView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class StatisticsCollector {
    private static final Logger LOGGER = Logger.getLogger(StatisticsCollector.class.getName());
    private static final int MAX_LOG_SIZE = 10000;

    private List<StatsEntry> log = new ArrayList<>();

    private void log(String text) {
        logFine(LOGGER, text);

        synchronized (log) {
            log.add(new StatsEntry(text));

            if (log.size() >= MAX_LOG_SIZE) {
                send();
            }
        }
    }

    private void send() {
        List<StatsEntry> log0;
        synchronized (log) {
            log0 = new ArrayList<>(log);
            log.clear();
        }

        if (!log0.isEmpty() && getPreferences().doSubmitStatistics()) {
            new SubmitStatsJob(log0).schedule();
        }
    }

    public void started() {
        // nothing useful to send here
    }

    public void logWarning(String message, Throwable ex) {
        String text = message;
        if (ex != null) {
            StringWriter w = new StringWriter();
            ex.printStackTrace(new PrintWriter(w, true));
            text += w.toString();
            text.replace("\n", "\\n").replace("\f", "\\f").replace("\r", "\\r");
        }

        log("warning " + text);
    }

    public void transformedViaAgent(String originalObjectSignature) {
        log("agent transformed " + originalObjectSignature);
    }

    public void receivedFromTarget(VariableObject obj) {
        log("received " + obj);
    }

    public void selected(VariableObject obj) {
        if (obj != null) {
            log("Selected " + obj);
        }
    }

    public void selected(MirurView view) {
        if (view != null) {
            log("Selected view " + view.getName());
        }
    }

    public void shuttingDown() {
        send();
    }

    private static class StatsEntry {
        final long time;
        final String text;

        StatsEntry(String text) {
            time = System.currentTimeMillis();
            this.text = text;
        }
    }

    private static class SubmitStatsJob extends Job {
        private final List<StatsEntry> log;

        public SubmitStatsJob(List<StatsEntry> log) {
            super("Sending Anonymized Statistics");
            this.log = log;

            setPriority(SHORT);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            String url = "https://api.emailyak.com/v1/1ujk9wvvcuz2m83/json/send/email/";
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                // don't try for too long and block shutdown
                connection.setConnectTimeout(1000);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                try (OutputStream output = connection.getOutputStream()) {
                    logInfo(LOGGER, "Submitting statistics");
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
                logSevere(LOGGER, "Error submitting statistics", ex);
                // If the user is closing the workspace, I don't want to show a failure
                return Status.OK_STATUS;
            }
        }

        private String buildBody() {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            builder.append("\n");
            builder.append("\"FromName\": ");
            builder.append("\"mirur-plugin-stats\",");
            builder.append("\n");
            builder.append("\"FromAddress\": ");
            builder.append("\"mirur-statistics@mirur.simpleyak.com\",");
            builder.append("\n");
            builder.append("\"ToAddress\": ");
            builder.append("\"plugin-dropbox@mirur.io\",");
            builder.append("\n");
            builder.append("\"TextBody\": ");
            builder.append("\"");

            for (StatsEntry e : log) {
                builder.append(escapeJson(String.format("%d,%s%n", e.time, e.text)));
            }

            builder.append("\",");
            builder.append("\n");
            builder.append("\"Subject\": ");
            builder.append("\"mirur statistics\"");
            builder.append("\n");
            builder.append("}");

            return builder.toString();
        }
    }
}

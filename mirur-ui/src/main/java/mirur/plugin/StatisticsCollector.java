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
import static com.metsci.glimpse.util.logging.LoggerUtils.logSevere;
import static mirur.plugin.Activator.getPreferences;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import mirur.core.VariableObject;
import mirur.plugins.MirurView;

public class StatisticsCollector {
    private static final Logger LOGGER = Logger.getLogger(StatisticsCollector.class.getName());
    private static final int MAX_LOG_SIZE = 10000;

    private List<StatsEntry> log = new ArrayList<>();

    public StatisticsCollector() {
        log("Using Eclipse version " + System.getProperty("eclipse.buildId"));
        log("Using Mirur version " + Activator.getDefault().getBundle().getVersion());
    }

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
            try {
                List<String> logEntries = new ArrayList<>();
                for (StatsEntry e : log) {
                    logEntries.add(String.format("%d,%s", e.time, e.text));
                }

                new SubmitToMirurSupport().sendLog(logEntries);

                return Status.OK_STATUS;
            } catch (IOException ex) {
                logSevere(LOGGER, "Error submitting statistics", ex);
                // If the user is closing the workspace, I don't want to show a failure
                return Status.OK_STATUS;
            }
        }
    }
}

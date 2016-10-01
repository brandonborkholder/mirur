package mirur.plugin;

import static com.metsci.glimpse.util.logging.LoggerUtils.logInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;

public class SubmitToMirurSupport {
    private static final Logger LOGGER = Logger.getLogger(SubmitToMirurSupport.class.getName());

    public void sendLog(List<String> logEntries) throws IOException {
        logInfo(LOGGER, "Sending anonymous log");

        StringBuilder builder = new StringBuilder();
        for (String s : logEntries) {
            builder.append(s);
            builder.append("\n");
        }

        send(builder.toString());
    }

    public void sendMirurRequest(String request) throws IOException {
        logInfo(LOGGER, "Sending Mirur request");
        send(request);
    }

    private void send(String body) throws IOException {
        String url = "https://docs.google.com/forms/d/e/1FAIpQLSddvcMv_4j5RdpmdgfLC6v1MaP9SyHAcUMvCIRAwT6sRTd2Zw/formResponse";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        // don't try for too long and block shutdown
        connection.setConnectTimeout(1000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream output = connection.getOutputStream()) {
            String postBody = "entry.947141406=" + URLEncoder.encode(body, "utf8");
            output.write(postBody.getBytes());
        }

        try (InputStream response = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response));
            StringBuilder responseBody = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }

            logInfo(LOGGER, "Response code %d", connection.getResponseCode());
        }
    }
}

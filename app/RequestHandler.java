import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class RequestHandler {
    public static String sendGetRequest(String requestURL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();
        } catch (Exception e) { e.printStackTrace(); }
        return sb.toString();
    }

    public static String sendPostRequest(String requestURL, Map<String, String> postData) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            StringBuilder postDataString = new StringBuilder();
            for (Map.Entry<String, String> entry : postData.entrySet()) {
                if (postDataString.length() != 0) postDataString.append("&");
                postDataString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            writer.write(postDataString.toString());
            writer.flush();
            writer.close();
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();
        } catch (Exception e) { e.printStackTrace(); }
        return sb.toString();
    }
}

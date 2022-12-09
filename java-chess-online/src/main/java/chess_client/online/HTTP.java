package chess_client.online;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class HTTP {
    private static final MediaType JSON = MediaType.parse("application/json");

    /**
     * Make a GET request to an external endpoint
     * 
     * @param URL  URL to GET to
     * @param data HashMap<String, String> containing parameters which will be added
     *             to URL
     * @return Response from server in plaintext
     * @throws IOException
     */
    public static String GET(String URL, HashMap<String, String> data) throws IOException {
        String appendedURL = new String(URL);
        for (Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (appendedURL.equals(URL)) {
                appendedURL += "?";
            } else {
                appendedURL += "&";
            }
            appendedURL += key + "=" + value;
        }

        Request request = new Request.Builder().url(appendedURL).build();
        try {
            Response response = longClient().newCall(request).execute();
            return response.body().string();
        } catch (java.net.ConnectException e) {
            System.out.println("Could not GET to URL: \"" + URL + "\"");
            System.exit(1);
            return null;
        }
    }

    /**
     * Make a POST request to an external endpoint
     * 
     * @param URL    URL to POST to
     * @param params HashMap<String, String> containing parameters which will be
     *               posted in the request body
     * @return Response from server in plaintext
     * @throws IOException
     */
    public static String POST(String URL, HashMap<String, String> params) throws IOException {
        String json = new JSONObject(params).toString();
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(URL).post(body).build();
        try {
            Response response = longClient().newCall(request).execute();
            return response.body().string();
        } catch (java.net.ConnectException e) {
            System.out.println("Could not POST to URL: \"" + URL + "\"");
            System.exit(1);
            return null;
        }
    }

    /**
     * Long client is an extension OkHttpClient which has a timeout of 30 seconds
     * 
     * @return OkHttpClient with 30 second timeout
     */
    private static OkHttpClient longClient() {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(5, TimeUnit.MINUTES);
        client.setWriteTimeout(5, TimeUnit.MINUTES);
        client.setConnectTimeout(5, TimeUnit.MINUTES);
        return client;
    }

    /**
     * Simplifies making a HashMap of params with username
     * 
     * @param usnm
     * @return
     */
    public static HashMap<String, String> MakeParamsWithUsnm(String usnm) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("usnm", usnm);
        return params;
    }

}

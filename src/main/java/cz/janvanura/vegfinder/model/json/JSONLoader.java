package cz.janvanura.vegfinder.model.json;

import android.util.AndroidException;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jan on 1. 4. 2015.
 */
public class JSONLoader {


    public static JSONArray getJSONFromUrl(String url) throws JSONException, IOException {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        HttpResponse response = client.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();

        int statusCode = statusLine.getStatusCode();

        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray jArray = new JSONArray(builder.toString());
            return jArray;
        }
        else {
            throw new IOException("Server did NOT response with code 200.");
        }
    }

}

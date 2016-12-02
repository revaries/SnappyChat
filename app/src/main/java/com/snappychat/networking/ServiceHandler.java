package com.snappychat.networking;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import org.json.JSONObject;

//

/**
 * Created by Fabrizio on 1/12/2016.
 */

public class ServiceHandler {

    public static final String TAG = "ServiceHandler";
    private static final String ENDPOINT_USER = "https://snappychatapi.herokuapp.com/api/users";

    // Return a JsonObject which is a key,value pair of the JSON string
    public static JsonObject getUser(String param) {
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(param)
                    .build().toString();
            String response = makeRequest(url,"GET");
            if(response != null){
                JsonArray jsonArray = new JsonParser().parse(response).getAsJsonArray();
                return jsonArray.get(0).getAsJsonObject();
            }

        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch user", e);
        }

        return null;
    }


    private static String makeRequest (String urlSpec, String method) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            connection.setRequestMethod(method);
            if(connection.getResponseCode() != 200  && connection.getResponseCode() != 201 && connection.getResponseCode() != 204) {
                Log.d(TAG, connection.getResponseMessage());
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            return sb.toString();
        } finally {
            connection.disconnect();
        }
    }

}

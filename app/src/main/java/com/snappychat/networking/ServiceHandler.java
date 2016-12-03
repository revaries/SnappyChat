package com.snappychat.networking;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    public static void updateUser(String param, JsonObject json) {
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(param)
                    .build().toString();

            String response = makeRequest(url,"PUT",json.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (IOException e) {
            Log.e(TAG, "Failed to update user", e);
        }
    }

    // Return a JsonObject which is a key,value pair of the JSON string
    public static JsonObject getUser(String param) {
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(param)
                    .build().toString();
            String response = makeRequest(url,"GET",null);
            if(response != null){
                JsonArray jsonArray = new JsonParser().parse(response).getAsJsonArray();
                return jsonArray.get(0).getAsJsonObject();
            }

        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch user", e);
        }

        return null;
    }

    private static String makeRequest (String urlSpec, String method, String jsonRequest) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        String response = null;
        try {
            connection.setRequestMethod(method);
            if (jsonRequest != null) {
                connection.setRequestProperty("Content-Type","application/json");
                connection.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonRequest);
                out.close();
            }
            if (connection.getResponseCode() != 200 && connection.getResponseCode() != 201 && connection.getResponseCode() != 204) {
                Log.d(TAG, "Response: "+connection.getResponseCode()+": "+connection.getResponseMessage());
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            response = sb.toString();
        }catch(Exception ex){
            Log.e("Error making request: "+urlSpec,ex.getMessage());
        } finally {
            connection.disconnect();
        }

        return response;
    }

}

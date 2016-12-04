package com.snappychat.networking;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.snappychat.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//import org.json.JSONObject;

//

/**
 * Created by Fabrizio on 1/12/2016.
 */

public class ServiceHandler {

    public static final String TAG = "ServiceHandler";
    private static final String ENDPOINT_USER = "https://snappychatapi.herokuapp.com/api/users";


    // Return a JsonObject which is a key,value pair of the JSON string
    public static void updateUser(String param, JSONObject json) {
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(param)
                    .build().toString();

            String response = makeRequest(url,"PUT",json.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to update user", e);
        }
    }

    public static ArrayList<User> getUsers(User userLoggedIn, String param){
        ArrayList<User> users = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendQueryParameter("search",param)
                    .build().toString();
            String response = makeRequest(url,"GET",null);
            if(response != null){
                JSONArray jsonArray = new JSONArray(response);
                users = new ArrayList<User>();
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if(jsonObject.optString("first_name") != "") {
                        User user = new Gson().fromJson(jsonObject.toString(), User.class);
                        user.setFriend(userLoggedIn.getFriends().contains(user));
                        if(jsonObject.optJSONObject("image") != null){
                            user.setImage(jsonObject.optJSONObject("image").optString("data"));
                        }
                        users.add(user);
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch user", e);
        }

        return users;
    }

    // Return a JSONObject which is a key,value pair of the JSON string
    public static User getUser(String param) {
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(param)
                    .build().toString();
            String response = makeRequest(url,"GET",null);
            if(response != null){
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                User user = new Gson().fromJson(jsonObject.toString(), User.class);
                if(jsonObject.optJSONObject("image") != null){
                    user.setImage(jsonObject.optJSONObject("image").optString("data"));
                }
                return user;
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch user", e);
        }

        return null;
    }

    public static String makeRequest (String urlSpec, String method, String jsonRequest) throws IOException {
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

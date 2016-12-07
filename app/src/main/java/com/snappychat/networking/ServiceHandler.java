package com.snappychat.networking;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snappychat.model.Timeline;
import com.snappychat.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import org.json.JSONObject;

//

/**
 * Created by Fabrizio on 1/12/2016.
 */

public class ServiceHandler {

    public static final String TAG = "ServiceHandler";
    private static final String ENDPOINT_USER = "https://snappychatapi.herokuapp.com/api/users";
    public static final String FRIENDS_URL = "friends";
    public static final String FRIENDS_REQUEST_URL = "friends_request";
    public static final String FRIENDS_PENDING_URL = "friends_pending";
    private static final String FIRABASE_SERVER_KEY =
            "key=AAAAtfwALJk:APA91bHXBxoCZ8_kuK5ML6CCfVBEMezJ7DMHft8fGpriIK5wGD4Fj07pDktiIT16bIyy" +
                    "MyMyHHHwYBMU8Jxcb5cVYyEoaGoLwzCHv3gW8fAV49zBachwromew0ms3YbU493p8wfJHZ8eJ_-_" +
                    "w57KqFJzzgp7mDxg_A";



    // Return a JsonObject which is a key,value pair of the JSON string
    public static String updateUser(String param, JSONObject json) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(param)
                    .build().toString();

            response = makeRequest(url,"PUT",json.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to update user", e);
        }
        return response;
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

    public static ArrayList<User> getFriends(String user_id){
        ArrayList<User> friends = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(user_id)
                    .appendPath(FRIENDS_URL)
                    .build().toString();
            String response = makeRequest(url,"GET",null);
            if(response != null){
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if(jsonObject.optJSONArray("friends") != null) {
                    Type listType = new TypeToken<List<User>>(){}.getType();
                    friends = new Gson().fromJson(jsonObject.optJSONArray("friends").toString(), listType);
                    for(User friend : friends){friend.setFriend(true);};
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch user friends", e);
        }

        return friends;
    }

    public static ArrayList<User> getFriendsRequest(String user_id){
        ArrayList<User> friends = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(user_id)
                    .appendPath(FRIENDS_REQUEST_URL)
                    .build().toString();
            String response = makeRequest(url,"GET",null);
            if(response != null){
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if(jsonObject.optJSONArray("friends_requested") != null) {
                    friends = new ArrayList<User>();
                    for(int i = 0; i < jsonObject.optJSONArray("friends_requested").length(); i++){
                        JSONObject js = jsonObject.optJSONArray("friends_requested").getJSONObject(i);
                        User user = new Gson().fromJson(js.getJSONObject("user_id").toString(),User.class);
                        if(js.optString("message") != null)
                            user.setMessage(js.optString("message"));
                        friends.add(user);
                    }
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch user", e);
        }

        return friends;
    }

    public static ArrayList<User> getFriendsPending(String user_id){
        ArrayList<User> friends = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(user_id)
                    .appendPath(FRIENDS_PENDING_URL)
                    .build().toString();
            String response = makeRequest(url,"GET",null);
            if(response != null){
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if(jsonObject.optJSONArray("friends_pending") != null) {
                    friends = new ArrayList<User>();
                    for(int i = 0; i < jsonObject.optJSONArray("friends_pending").length(); i++){
                        JSONObject js = jsonObject.optJSONArray("friends_pending").getJSONObject(i);
                        User user = new Gson().fromJson(js.getJSONObject("user_id").toString(),User.class);
                        if(js.optString("message") != null)
                            user.setMessage(js.optString("message"));
                        friends.add(user);
                    }
                }

            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch pending requests", e);
        }

        return friends;
    }

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


    public static String addFriend(String user_id, JSONObject json) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(user_id)
                    .appendPath(FRIENDS_REQUEST_URL)
                    .build().toString();

            response = makeRequest(url,"POST",json.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to add Friend", e);
        }
        return response;
    }

    public static String deleteFriend(String user_id, JSONObject json) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(user_id)
                    .appendPath(FRIENDS_URL)
                    .build().toString();

            response = makeRequest(url,"DELETE",json.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to delete user", e);
        }
        return response;
    }

    public static String deleteFriendRequest(String user_id, JSONObject json) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(user_id)
                    .appendPath(FRIENDS_REQUEST_URL)
                    .build().toString();

            response = makeRequest(url,"DELETE",json.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to update user", e);
        }
        return response;
    }

    public static String updateFriendPending(String user_id, JSONObject json) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(user_id)
                    .appendPath(FRIENDS_PENDING_URL)
                    .build().toString();

            response = makeRequest(url,"POST",json.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to update user", e);
        }
        return response;
    }

    public static ArrayList<Timeline> getTimeline(String param) {
        ArrayList<Timeline> timelines = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(param)
                    .appendPath("timeline")
                    .build().toString();
            String response = makeRequest(url,"GET",null);
            if(response != null){
                JSONArray jsonArray = new JSONArray(response);
                timelines = new ArrayList<Timeline>();
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Timeline timeline = new Gson().fromJson(jsonObject.toString(), Timeline.class);

                    if(jsonObject.optJSONArray("images") != null){
                        timeline.setImages(new ArrayList<String>());
                        for(int j=0; i < jsonObject.optJSONArray("images").length(); j++){
                            JSONObject jsonObject1 =  jsonObject.optJSONArray("images").getJSONObject(i);
                            timeline.getImages().add(jsonObject.optString("data"));
                        }
                    }
                    timelines.add(timeline);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch timeline", e);
        }

        return timelines;
    }

    public static String makeRequest (String urlSpec, String method, String jsonRequest) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        String response = null;
        try {
            connection.setRequestMethod(method);
            if (jsonRequest != null) {
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Authorization", FIRABASE_SERVER_KEY);
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

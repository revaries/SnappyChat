package com.snappychat.networking;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    public static final String ENDPOINT_USER = "https://snappychatapi.herokuapp.com/api/users";
    public static final String ENDPOINT_CHAT = "https://snappychatapi.herokuapp.com/api/chats";
    public static final String FRIENDS_URL = "friends";
    public static final String FRIENDS_REQUEST_URL = "friends_request";
    public static final String FRIENDS_PENDING_URL = "friends_pending";
    private static final String FIRABASE_SERVER_KEY =
            "key=AAAARKxcUQ8:APA91bEQZuqZeh6e3CLk1oSXbwY5EtlPz3uHixdmQLu67_LpfOinSZN3ua3hE35-HZMhJOMXT-CGCKs5kv3R9PMT33E82WZPqk1bMFxOewViTwn_VoU5619Cg9xrCXIcNXvqAZGx0x2aG2n6ddnFJubdaI7kus6L2A";




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


    public static String updateUserWithString (String param, String json) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(param)
                    .build().toString();
            Log.v("JSON",json);
            response = makeRequest(url,"PUT",json);
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to update user", e);
        }
        return response;
    }





    //Creating a Method to Create User
    public static String createUser(String json) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .build().toString();

            response = makeRequest(url,"POST",json);
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
                    .appendQueryParameter("email",userLoggedIn.getEmail())
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
                    for(int i=0; i < friends.size(); i++){
                        User friend = friends.get(i);
                        JSONObject jsonObject1 = jsonObject.optJSONArray("friends").getJSONObject(i);
                        if(jsonObject1.optJSONObject("image") != null){
                            friend.setImage(jsonObject1.optJSONObject("image").optString("data").trim());
                        }
                        friend.setFriend(true);
                    };
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

    public static String updateChat(String url,JSONObject json) {
        String response = null;
        try {
            //String url = Uri.parse(ENDPOINT_CHAT).buildUpon()
            //        .appendEncodedPath(url)
            //        .build().toString();

            response = makeRequest(url,"PUT",json.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to update user", e);
        }
        return response;
    }


    public static String createTimeline(String userId, String json) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_USER).buildUpon()
                    .appendEncodedPath(userId)
                    .appendPath("timeline")
                    .build().toString();

            response = makeRequest(url,"POST",json);
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
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                for(int i=0; i < jsonObject.getJSONArray("timeline").length(); i++){
                    JSONObject jsonObject1 = jsonObject.getJSONArray("timeline").getJSONObject(i);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
                    Timeline timeline = gson.fromJson(jsonObject1.toString(), Timeline.class);
                    timelines.add(timeline);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch timeline", e);
        }

        return timelines;
    }

    public static ArrayList<User> getChatConversations(String user_id){
        ArrayList<User> chats = new ArrayList<>();
        try {
            String url = Uri.parse(ENDPOINT_CHAT+"?search="+user_id).buildUpon()
                    .build().toString();
            String response = makeRequest(url,"GET",null);
            if(response != null){
                JSONArray jsonArray = new JSONArray(response);
                User user = null;
                for(int i=0; i < jsonArray.length(); i++){
                    Boolean receiver = false;
                    Boolean chatOwner = false;
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String chat_id = jsonObject.getString("_id");
                    String chat_creator = jsonObject.getJSONObject("user_creator_id").getString("email");
                    if(chat_creator.equals(user_id)){
                        chatOwner = true;
                    }
                    JSONArray chat_messages = jsonObject.getJSONArray("chat_messages");
                    JSONObject last_message = chat_messages.getJSONObject(chat_messages.length() - 1);

                    String user_sender = last_message.getJSONObject("user_sender_id").getString("email");
                    String user_receiver = last_message.getJSONObject("user_receiver_id").getString("email");
                    Boolean pending = jsonObject.getBoolean("pending");
                    String message = last_message.getString("message");
                    String messageType = last_message.getString("type");
                    if(!user_sender.equals(user_id)){
                        user = getUser(user_sender);
                    }else if(!user_receiver.equals(user_id)){
                        user = getUser(user_receiver);
                    }
                    if(user_receiver.equals(user_id) && pending){
                        receiver = true;
                    }
                    user.setMessageType(messageType);
                    user.setMessage(message);
                    user.setPending(receiver);
                    user.setChatOwner(chatOwner);
                    user.setChatConversationId(chat_id);
                    chats.add(user);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch chat conversations", e);
        }

        return chats;
    }

    public static String deleteChatConversation(JSONObject jsonObject) {
        String response = null;
        try {
            String url = Uri.parse(ENDPOINT_CHAT).buildUpon()
                    .build().toString();
            response = makeRequest(url,"DELETE",jsonObject.toString());
            if(response != null){
                Log.v(TAG,response);
            }

        } catch (Exception e) {
            if(response != null) {
                Log.e(TAG, "Failed to delete chat conversation", e);
            }
        }
        return response;
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

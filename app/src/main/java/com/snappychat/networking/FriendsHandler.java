package com.snappychat.networking;

import android.net.Uri;
import android.util.Log;
import com.snappychat.friends.FriendCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Jelson on 12/3/2016.
 */

public class FriendsHandler {
    public static final String API_URL = "https://snappychatapi.herokuapp.com/api/users";
    private static final String CURRENT_USER = "jesantos0527@gmail.com";
    private static final String CURRENT_USER_ID = "58446f5489866a0004ecfe11";
    private static final String TAG = "FRIENDS_HANDLER";

    public static final String FRIENDS_URL = API_URL + "/" + CURRENT_USER + "/friends";
    public static final String PENDING_URL = API_URL + "/" + CURRENT_USER + "/friends_pending";
    public static final String REQUESTED_URL = API_URL + "/" + CURRENT_USER + "/friends_request";

    public static String getFriends(String apiURL){
        String url = Uri.parse(apiURL).buildUpon()
                .build().toString();
        Log.d(TAG, "URL: "+url);
        String response = null;
        try{
            response = ServiceHandler.makeRequest(url,"GET",null);
        }catch (Exception e) {
            if(e.getMessage() != null){
                Log.e(TAG, e.getMessage());
            }
        }
        return response;

    }

    public static ArrayList<FriendCard> processJsonResponse(String response, String type){
        ArrayList<FriendCard> friendCards = new ArrayList<>();
        Log.d(TAG, "Process Response - "+response);
        if(response != null){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject parser = jsonArray.getJSONObject(i);
                    String type_array = "";
                    if(type.equals(FRIENDS_URL)){
                        type_array = "friends";
                    }else if(type.equals(PENDING_URL)){
                        type_array = "friends_pending";
                    }else if(type.equals(REQUESTED_URL)){
                        type_array = "friends_requested";
                    }
                    JSONArray friendArray = parser.getJSONArray(type_array);
                    for(int j=0; j < friendArray.length(); j++){
                        JSONObject p = friendArray.getJSONObject(j);
                        friendCards.add(handleJsonObj(p, type_array));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return friendCards;
    }

    public static FriendCard handleJsonObj(JSONObject parser, String type){
        if(type.equals("friends")){
            return handleCurrentFriends(parser);
        }else if(type.equals("friends_pending")){
            return handlePendingFriends(parser);
        }else if(type.equals("friends_requested")){
            return handlePendingFriends(parser);
        }
        return null;
    }

    public static FriendCard handleCurrentFriends(JSONObject parser){
        String name = "", last = "", email = "", description = "";
        try {
            name = parser.getString("first_name");
            last = parser.getString("last_name");
            email = parser.getString("email");
            //description = p.getString("description");
            description = "This is a description";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new FriendCard(name, last, email, description, null, false, false, false);
    }

    public static FriendCard handlePendingFriends(JSONObject parser){
        try {
            JSONObject user = parser.getJSONObject("user_id");
            return handleCurrentFriends(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<FriendCard> processFriendsJsonResponse(String response, String type){
        ArrayList<FriendCard> friendCards = new ArrayList<>();
        Log.d(TAG, "Process Response - "+response);
        if(response != null){
            try {
                JSONArray jsonArray = new JSONArray(response);
                //Log.d(TAG, "Object "+jsonArray.toString(2));
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject parser = jsonArray.getJSONObject(i);
                    //Log.d(TAG, "Object "+parser.toString());
                    String name = parser.getString("first_name");
                    String last = parser.getString("last_name");
                    String email = parser.getString("email");
                    String description = "This is a description";
                    String visibility = parser.getString("visibility");
                    JSONArray friends = parser.getJSONArray("friends");
                    JSONArray pending = parser.getJSONArray("friends_pending");
                    JSONArray requested = parser.getJSONArray("friends_requested");
                    //Log.d(TAG, "friends "+friends.toString());
                    Boolean areFriends = false;
                    Boolean arePending = false;
                    Boolean areRequested = false;
                    if(friends.toString().contains(CURRENT_USER_ID)){
                        //Log.d(TAG, name + " are friends");
                        areFriends = true;
                    }
                    for(int j=0; j < pending.length(); j++){
                        JSONObject p = pending.getJSONObject(j);
                        if(p.getString("user_id").equals(CURRENT_USER_ID)){
                            //Log.d(TAG, name + " are pending friends");
                            arePending = true;
                        }
                    }
                    for(int j=0; j < requested.length(); j++){
                        JSONObject p = requested.getJSONObject(j);
                        if(p.getString("user_id").equals(CURRENT_USER_ID)){
                            //Log.d(TAG, name + " are requested friends");
                            areRequested = true;
                        }
                    }
                    friendCards.add(new FriendCard(name,last,email,description,visibility,areFriends,arePending,areRequested));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return friendCards;
    }

}

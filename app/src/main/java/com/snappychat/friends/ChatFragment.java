package com.snappychat.friends;

import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.snappychat.R;
import com.snappychat.model.ChatMessage;
import com.snappychat.networking.FriendsHandler;
import com.snappychat.networking.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Jelson on 11/27/16.
 */

public class ChatFragment extends Fragment{//implements OnClickListener {
    private static final String GOOGLE_API_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String CHAT_URL = "https://snappychatapi.herokuapp.com/api/chats";
    //private static final String SENDER_ID = "1071236147570";
    private EditText msg_edittext;
    //private static String current_user;
    //private static String chat_friend;
    private static String getMessageUrl;
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;
    private ImageButton mSendImageButton;
    private ImageView mAddImage;

    //temporary destination key
    private static String token;
    //send message
    private static final String TAG_SEND = "MessageSender";
    private AsyncTask<Void, Void, String> requestTask;
    private AsyncTask<String, Void, ArrayList<ChatMessage>> getMessagesTask;
    //AtomicInteger ccsMsgId = new AtomicInteger();
    //FirebaseMessaging fm = FirebaseMessaging.getInstance();

    private static final String TAG = "UPDATE_VIEW";

    public static AsyncTask<Void, Void, Void> updateViewTask;
    public String chatId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        random = new Random();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Created View Token - " + refreshedToken);

        ///SETUP USERS FROM INTENT HERE;
        //current_user = "jesantos0527@gmail.com";
        //chat_friend = "fabriziojps@gmail.com";
        getMessageUrl = "https://snappychatapi.herokuapp.com/api/chats?user_sender_id=" +
                ((ChatActivity)getActivity()).userSender.getEmail() + "&user_receiver_id=" + ((ChatActivity)getActivity()).userReceiver.getEmail();
        //token = getToken(chat_friend);
        token = ((ChatActivity)getActivity()).userReceiver.getToken();
        //token = ((ChatActivity)getActivity()).userSender.getToken();
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sendMessageButton:
                        sendTextMessage();
                }
            }
        });

        mSendImageButton = (ImageButton) view.findViewById(R.id.sendImageButton);
        mAddImage = (ImageView) view.findViewById(R.id.image_upload);
        mSendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAddImage.getVisibility() == View.GONE){
                    mAddImage.setVisibility(View.VISIBLE);
                }else{
                    mAddImage.setVisibility(View.GONE);
                }
            }
        });

        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);
        return view;
    }

    public String getToken(final String email){
        requestTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String response = null;
                try {

                    String url = Uri.parse(FriendsHandler.API_URL+ "/" + email).buildUpon()
                            .build().toString();
                    response = ServiceHandler.makeRequest(url,"GET",null);
                    JSONArray jsonArray = new JSONArray(response);
                    token = jsonArray.getJSONObject(0).getString("token");
                    //Log.d(TAG, "Token JSON "+jsonArray.getJSONObject(0).getString("token"));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Token is "+token;
            }

            @Override
            protected void onPostExecute(String result) {
                requestTask = null;
            }

        };
        requestTask.execute(null, null, null);
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    public void getHistoryChatMessages(){
        Log.d(TAG, "Entering update view.");
        getMessagesTask = new AsyncTask<String, Void, ArrayList<ChatMessage>>() {
            @Override
            protected ArrayList<ChatMessage> doInBackground(String... params) {
                String getMessages = null;
                ArrayList<ChatMessage> messages = null;
                try {
                    getMessages = ServiceHandler.makeRequest(getMessageUrl,"GET",null);
                    JSONArray jsonArray = new JSONArray(getMessages);
                    JSONObject parser = jsonArray.getJSONObject(0);
                    JSONArray chatMessages = parser.getJSONArray("chat_messages");
                    Log.d(TAG, "ChatMessages "+ chatMessages);
                    boolean pending = parser.getBoolean("pending");
                    messages = new ArrayList<ChatMessage>();
                    for(int i=0; i < chatMessages.length(); i++){
                        JSONObject message = chatMessages.getJSONObject(i);
                        String sender_email = message.getJSONObject("user_sender_id").getString("email");
                        String receiver_email = message.getJSONObject("user_receiver_id").getString("email");
                        String msg = message.getString("message");
//                        Log.d(TAG, "Sender id "+ sender_email);
//                        Log.d(TAG, "Receiver id "+ receiver_email);
//                        Log.d(TAG, "Message "+ msg);
                        Boolean isMine = false;
                        if(sender_email.equals(((ChatActivity)getActivity()).userSender.getEmail())){
                            isMine= true;
                        }
                        ChatMessage chatMessage = new ChatMessage(sender_email, receiver_email,
                                msg, "" + random.nextInt(1000), isMine);
                        chatMessage.setChatId(message.optString("chat_id"));
                        messages.add(chatMessage);
                        //chatAdapter.add(chatMessage);
                    }
                    String receiver_email = messages.get(messages.size()-1).getReceiver();
                    //Verify if there are pending messages and the checker is the receiver,
                    //so only the receiver can update the status of the conversation
                    if(pending && receiver_email.equals(params[0])) {
                        //Update the chat conversation status
                        JSONObject chat = new JSONObject();
                        chat.put("pending", false);
                        ServiceHandler.updateChat(ServiceHandler.ENDPOINT_CHAT+"?user_sender_id="+params[0]+"&user_receiver_id="+params[1], chat);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "Response Chat "+getMessages);
                return messages;
            }

            @Override
            protected void onPostExecute(ArrayList<ChatMessage> result) {
                if(result != null){
                    chatId = result.get(0).getChatId();
                    chatAdapter.updateData(result);
                }
            }

        };
        getMessagesTask.execute(((ChatActivity)getActivity()).userSender.getEmail(), ((ChatActivity)getActivity()).userReceiver.getEmail());
    }


    public void sendTextMessage() {
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage(((ChatActivity)getActivity()).userSender.getEmail(), ((ChatActivity)getActivity()).userReceiver.getEmail(),
                    message, "" + random.nextInt(1000), true);
            chatMessage.setMsgID();
            chatMessage.setChatId(chatId);
            chatMessage.body = message;
            chatMessage.Date = getCurrentDate();
            chatMessage.Time = getCurrentTime();
            msg_edittext.setText("");
            //updateView(chatMessage);
            postMessage(chatMessage);
            updateViewAndChatStatus(chatMessage,true);
        }
    }

    public void postToDatabase(ChatMessage chatMessage){
        JSONObject updateDB = new JSONObject();
        String responseChat;
        try {
            updateDB.put("user_sender_id", chatMessage.getSender());
            updateDB.put("user_receiver_id", chatMessage.getReceiver());
            updateDB.put("message", chatMessage.body);
            String chatUrl = Uri.parse(CHAT_URL).buildUpon()
                    .build().toString();
            responseChat = ServiceHandler.makeRequest(chatUrl,"POST",updateDB.toString());
            Log.d(TAG, "Updated to database: "+responseChat);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void postMessage (final ChatMessage chatMessage){
        updateViewTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                String response = null;
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    data.put("message", chatMessage.body);
                    data.put("user_sender_id", chatMessage.getSender());
                    data.put("user_receiver_id", chatMessage.getReceiver());
                    json.put("data", data);
                    json.put("to", token);

                    String url = Uri.parse(GOOGLE_API_URL).buildUpon()
                            .build().toString();
                    response = ServiceHandler.makeRequest(url,"POST",json.toString());
                    postToDatabase(chatMessage);
                    if(response != null){
                        Log.v(TAG,"Send Message response is "+response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //chatAdapter.add(chatMessage);
                //return "Chat message added!. Response "+response;
                return null;
            }


        };
        updateViewTask.execute();
    }

    public void updateViewAndChatStatus(final ChatMessage chatMessage,boolean status){
        chatAdapter.add(chatMessage);
        updateChat(chatMessage.getSender(),chatMessage.getReceiver(),status);
    }

    public void updateChat(final String userSenderEmail, String userReceiverEmail, boolean status){
        AsyncTask<Object,Void,Void> asyncTask = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                JSONObject chat = new JSONObject();
                try {
                    chat.put("pending", (Boolean) params[2]);
                    ServiceHandler.updateChat(ServiceHandler.ENDPOINT_CHAT+"?user_sender_id="+params[0]+"&user_receiver_id="+params[1], chat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        asyncTask.execute(userSenderEmail,userReceiverEmail,status);
    }

    public static String getCurrentTime(){
        Date today = Calendar.getInstance().getTime();
        DateFormat format_day = new SimpleDateFormat("K:mma", Locale.US);
        return format_day.format(today);
    }

    public static String getCurrentDate(){
        Date today = Calendar.getInstance().getTime();
        DateFormat format_date = new SimpleDateFormat("d MMM yyyy", Locale.US);
        return format_date.format(today);
    }

}



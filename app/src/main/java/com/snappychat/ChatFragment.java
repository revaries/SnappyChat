package com.snappychat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Jelson on 11/27/16.
 */

public class ChatFragment extends Fragment{//implements OnClickListener {
    private static final String SENDER_ID = "1071236147570";
    private EditText msg_edittext;
    private String user1 = "khushi1", user2 = "khushi2";
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;


    //send message
    private static final String TAG_SEND = "MessageSender";
    AsyncTask<Void, Void, String> sendTask;
    AtomicInteger ccsMsgId = new AtomicInteger();
    FirebaseMessaging fm = FirebaseMessaging.getInstance();

    private static final String TAG = "UPDATE_VIEW";

    public static AsyncTask<Void, Void, String> updateViewTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        random = new Random();
        //((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
        //  "Chats");
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sendMessageButton:
                        sendTextMessage(v);

                }
            }
        });

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }


    public void sendMessage(final Bundle data) {

        sendTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String msgId = Integer.toString(ccsMsgId.incrementAndGet());
                Log.d(TAG, "messageid: " + msgId);
                fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                        .setMessageId(msgId)
                        .addData("my_message", "Hello World")
                        .addData("my_action","SAY_HELLO")
                        .build());
                Log.d(TAG, "After fcm.send successful.");
                /*try {
                    Log.d(TAG, "messageid: " + msgId);
                    fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                            .setMessageId(msgId)
                            .addData("my_message", "Hello World")
                            .addData("my_action","SAY_HELLO")
                            .build());
                    Log.d(TAG, "After fcm.send successful.");
                } catch (IOException e) {
                    Log.d(TAG, "Exception: " + e);  
                    e.printStackTrace();
                }*/
                return "Message ID: "+msgId+ " Sent.";
            }

            @Override
            protected void onPostExecute(String result) {
                sendTask = null;
                Log.d(TAG, "onPostExecute: result: " + result);
            }

        };
        sendTask.execute(null, null, null);
    }


    public void sendTextMessage(View v) {
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage(user1, user2,
                    message, "" + random.nextInt(1000), true);
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = getCurrentDate();
            chatMessage.Time = getCurrentTime();
            msg_edittext.setText("");
            //chatAdapter.add(chatMessage);
            //chatAdapter.notifyDataSetChanged();
            updateView(chatMessage);
        }
    }

    public static void updateView(final ChatMessage chatMessage){
        Log.d(TAG, "Entering update view.");
        updateViewTask = new AsyncTask<Void, Void, String>() {
            /*@Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(TAG, "onPreExecute");
            }*/

            @Override
            protected String doInBackground(Void... params) {

                Log.d(TAG, "Before update");
                chatAdapter.add(chatMessage);
                //Log.d(TAG, "After gcm.send successful.");
//                try {
//                    Log.d(TAG, "Before update");
//                    chatAdapter.add(chatMessage);
//                    Log.d(TAG, "After gcm.send successful.");
//                } catch (IOException e) {
//                    Log.d(TAG, "Exception: " + e);
//                    //e.printStackTrace();
//                }
                return "Chat message added!";
            }

            @Override
            protected void onPostExecute(String result) {
                chatAdapter.notifyDataSetChanged();
                updateViewTask = null;
                Log.d(TAG, "onPostExecute: result: " + result);
            }

        };
        Log.d(TAG, "Calling execute.");
        updateViewTask.execute(null, null, null);
        Log.d(TAG, "Leaving update view.");

        //chatAdapter.add(chatMessage);
        //chatAdapter.notifyDataSetChanged();
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



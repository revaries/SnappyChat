package com.snappychat.networking;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.snappychat.MainActivity;
import com.snappychat.friends.ChatFragment;
import com.snappychat.model.ChatMessage;
import com.snappychat.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.d(TAG, "onMessageReceived was call");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String message = remoteMessage.getData().get("message");
            String sender_id = remoteMessage.getData().get("user_sender_id");
            String receiver_id = remoteMessage.getData().get("user_receiver_id");
            String type = remoteMessage.getData().get("type");
            Random random = new Random();
            final ChatMessage chatMessage = new ChatMessage(sender_id, receiver_id,
                    message, "" + random.nextInt(1000), false, type);
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = ChatFragment.getCurrentDate();
            chatMessage.Time = ChatFragment.getCurrentTime();

            //Publish the chat message to be consumed by listeners
            EventBus.getDefault().post(new MessageEvent(chatMessage));

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                /*.setSmallIcon(R.drawable.ic_stat_ic_notification)*/
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        Log.d(TAG, "sendNotification called.");
    }



}
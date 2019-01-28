package com.liveperson.mobilemessagingexercise.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.content.Intent;
import android.util.Log;

import java.util.Map;

public class LpFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = LpFirebaseMessagingService.class.getSimpleName();

    public LpFirebaseMessagingService() {
        super();
        Log.d(TAG, "Constructor called");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate called");
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        Map<String, String> messageData = remoteMessage.getData();

        if (messageData.size() > 0) {
            Log.d(TAG, "Message data payload: ");
            for (Map.Entry<String, String> entry : messageData.entrySet()) {
                Log.d(TAG, "  " + entry.getKey() + " : " + entry.getValue());
            }

            // Send the data into the SDK
            //String account = "82055668";
            //PushMessage message = LivePerson.handlePushMessage(this, remoteMessage.getData(), account, false);

            //Code snippet to add push UI notification
            //if (message != null){
            // We'll work this out later NotificationUI.showNotification(this, message);
            //}

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(String s) {
        // Get updated InstanceID token.
        Intent intent = new Intent(this, LpFirebaseMessagingService.class);
        startService(intent);
        Log.d("NEW_TOKEN",s);
    }

}



package com.liveperson.mobilemessagingexercise.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.liveperson.infra.model.PushMessage;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.Fragments.MyAccountFragment;
import com.liveperson.mobilemessagingexercise.R;
import com.liveperson.mobilemessagingexercise.WelcomeActivity;
import com.liveperson.mobilemessagingexercise.model.ApplicationConstants;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Map;

import static com.liveperson.mobilemessagingexercise.model.ApplicationConstants.LP_IS_FROM_PUSH;

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
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        Map<String, String> messageData = remoteMessage.getData();

        if (messageData.size() > 0) {
            Log.d(TAG, "Message data payload: ");
            for (Map.Entry<String, String> entry : messageData.entrySet()) {
                Log.d(TAG, "  " + entry.getKey() + " : " + entry.getValue());
            }

            // Pass the data from the message into the SDK
            PushMessage pushMessage = LivePerson.handlePushMessage(this, remoteMessage.getData(),
                    ApplicationConstants.LIVE_PERSON_ACCOUNT_NUMBER, false);

            if (pushMessage != null) {
                showPushNotification(this, pushMessage);
            }

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

    private void showPushNotification(Context ctx, PushMessage pushMessage) {

        Notification.Builder builder = createNotificationBuilder(ctx, ApplicationConstants.LP_PUSH_NOTIFICATION_CHANNNEL_ID, "Push Notification", true);
        int unreadMessageCount = LivePerson.getNumUnreadMessages(pushMessage.getBrandId());

        builder.setContentIntent(createPendingIntent(ctx))
            .setContentTitle(pushMessage.getFrom() + getLeString(R.string.said))
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setNumber(pushMessage.getCurrentUnreadMessgesCounter())
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setPriority(Notification.PRIORITY_HIGH)
            .setStyle(new Notification.InboxStyle()
                .addLine(pushMessage.getMessage())
                .addLine(createUnreadMessageText(unreadMessageCount - 1)));

         getNotificationManager(ctx).notify(ApplicationConstants.LP_PUSH_NOTIFICATION_ID, builder.build());
    }

    private String createUnreadMessageText(int unreadMessageCount) {
        String unreadMessageCountStr;
        String unreadText = getLeString(R.string.unreadMessages);
        switch(unreadMessageCount) {
            case 0:
                unreadMessageCountStr = getLeString(R.string.no);
                break;
            case 1:
                unreadText = getLeString(R.string.unreadMessage);
            default:
                unreadMessageCountStr = Integer.toString(unreadMessageCount);
                break;
        }

        return(getLeString(R.string.youHave) + unreadMessageCountStr + unreadText);
    }

    private String getLeString(int id) {
        String baseString = getString(id);
        return baseString.replace('_', ' ');
    }

    /**
     * Create notification builder according to platform level.
     */
    private Notification.Builder createNotificationBuilder(Context ctx, String channelId, String channelName, boolean isHighImportance) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(ctx);
        } else {
            //Create a channel for the notification.
            createNotificationChannel(ctx, channelId, channelName, isHighImportance);
            builder = new Notification.Builder(ctx, channelId);
        }

        return builder;
    }

    /**
     * Creates a notification channel with the given parameters.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(Context context, String channelId, String channelName, boolean isHighImportance) {
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        if (isHighImportance) {
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        }
        getNotificationManager(context).createNotificationChannel(notificationChannel);
    }

    private PendingIntent createPendingIntent(Context ctx) {
        Intent showIntent = new Intent(ctx, WelcomeActivity.class);
        showIntent.putExtra(LP_IS_FROM_PUSH, true);

        return PendingIntent.getActivity(ctx, 0, showIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private NotificationManager getNotificationManager(Context ctx) {
        return (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
    }


}



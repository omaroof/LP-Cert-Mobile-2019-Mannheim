package com.liveperson.mobilemessagingexercise.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.liveperson.infra.ICallback;
import com.liveperson.infra.model.PushMessage;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.R;
import com.liveperson.mobilemessagingexercise.WelcomeActivity;
import com.liveperson.mobilemessagingexercise.model.ApplicationConstants;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

import java.util.Map;

import static com.liveperson.mobilemessagingexercise.model.ApplicationConstants.LP_IS_FROM_PUSH;

public class LpFirebaseMessagingService extends FirebaseMessagingService implements ICallback<Integer, Exception> {
    private static final String TAG = LpFirebaseMessagingService.class.getSimpleName();

    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication applicationInstance;
    private PushMessage pushMessage;

    public LpFirebaseMessagingService() {
        super();
        Log.d(TAG, "Constructor called");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate called");
        super.onCreate();

        //Link to the main application
        applicationInstance = (MobileMessagingExerciseApplication)getApplication();
        applicationStorage = ApplicationStorage.getInstance();
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
            pushMessage = LivePerson.handlePushMessage(this, remoteMessage.getData(),
                    ApplicationConstants.LIVE_PERSON_ACCOUNT_NUMBER, false);

            if (pushMessage != null) {
                showPushNotification(pushMessage);
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    /**
     * Process an updated Firebase push message token
     * @param fcmToken
     */
    @Override
    public void onNewToken(String fcmToken) {
        Log.d(TAG, "New Firebase token received: " + fcmToken);
        //Update the registration with the new token
        LivePerson.registerLPPusher(ApplicationConstants.LIVE_PERSON_ACCOUNT_NUMBER, ApplicationConstants.LIVE_PERSON_APP_ID,
                fcmToken, null, null);
    }

    /**
     * Notify the consumer of the arrival of the push message from LiveEngage
     * @param pushMessage The LivePerson push message
     */
    private void showPushNotification(PushMessage pushMessage) {
        //Get the count of unread messages
        LivePerson.getNumUnreadMessages(ApplicationConstants.LIVE_PERSON_APP_ID, this);
    }

    /**
     * Create the notification and show it to the consumer, once the count of unread messages is known
     * @param unreadMessageCount the number of unread messages.
     */
    @Override
    public void onSuccess(Integer unreadMessageCount) {
        Notification.Builder builder = createNotificationBuilder(this,
                ApplicationConstants.LP_PUSH_NOTIFICATION_CHANNNEL_ID, "Push Notification", true);

        builder.setContentIntent(createPendingIntent(this))
                .setContentTitle(pushMessage.getFrom() + getLeString(R.string.said))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setNumber(pushMessage.getCurrentUnreadMessgesCounter())
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setStyle(new Notification.InboxStyle()
                        .addLine(pushMessage.getMessage())
                        .addLine(createUnreadMessageText(unreadMessageCount.intValue() - 1)));

        getNotificationManager(this).notify(ApplicationConstants.LP_PUSH_NOTIFICATION_ID, builder.build());
    }

    /**
     * Can't get the number of unread messages
     * @param e
     */
    @Override
    public void onError(Exception e) {
        Log.e(TAG, "Unable to get count of unread messages", e);
    }


    /**
     * Create the text for the count of unread messages
     * @param messageNumber the number of unread messages
     * @return a message about the unread messages, suitable for the consumer
     */
    private String createUnreadMessageText(int messageNumber) {
        String messageNumberStr;
        String unreadText = getLeString(R.string.unreadMessages);
        switch(messageNumber) {
            case 0:
                messageNumberStr = getLeString(R.string.no);
                break;
            case 1:
                unreadText = getLeString(R.string.unreadMessage);
            default:
                messageNumberStr = Integer.toString(messageNumber);
                break;
        }

        return(getLeString(R.string.youHave) + messageNumberStr + unreadText);
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



package com.liveperson.mobilemessagingexercise.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.liveperson.infra.ICallback;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.model.ApplicationConstants;


/**
 * Register for LivePerson push messages
 */
public class LpFirebaseRegistrationIntentService extends IntentService implements OnCompleteListener<InstanceIdResult>, ICallback<Void, Exception> {
    public static final String TAG = LpFirebaseRegistrationIntentService.class.getSimpleName();

    /**
     * Default constructor
     */
    public LpFirebaseRegistrationIntentService() {
        super(TAG);
    }

    /**
     * Handle the Intent used to start the service
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //Retrieve the Firebase token to use
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this);

    }

    /**
     * Process the result of retrieving the FCM token for this app
     * @param task the task whose completion triggered this method being called
     */
    @Override
    public void onComplete(Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
            Log.w(TAG, "getInstanceId failed", task.getException());
            return;
        }

        // Get new Instance ID token
        String fcmToken = task.getResult().getToken();

        // Log and toast the token value
        Log.d(TAG +  " Firebase token: ", fcmToken);

        //Register to receive push messages with the new token
        LivePerson.registerLPPusher(ApplicationConstants.LIVE_PERSON_ACCOUNT_NUMBER, ApplicationConstants.LIVE_PERSON_APP_ID,
                fcmToken, null, this);
    }

    /**
     * Registration for push messages with LiveEngage was successful
     * @param aVoid the parameter for the successful registration
     */
    @Override
    public void onSuccess(Void aVoid) {
        Log.d(TAG, "Registered for push notifications");
    }

    /**
     * Registration for push messages with LiveEngage failed
     * @param e the Exception associated with the failure
     */
    public void onError(Exception e) {
        Log.d(TAG, "Unable to register for push notifications");
    }
}

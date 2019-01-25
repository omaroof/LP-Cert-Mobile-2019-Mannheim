package com.liveperson.mobilemessagingexercise.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.model.ApplicationConstants;


/**
 * Register for LivePerson push messages
 */
public class LpFirebaseRegistrationIntentService extends IntentService {
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
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        // Log and toast the token value
        Log.d(TAG, fcmToken);

        LivePerson.registerLPPusher(ApplicationConstants.LIVE_PERSON_ACCOUNT_NUMBER,
                ApplicationConstants.LIVE_PERSON_APP_ID, fcmToken,
                null, null);
    }
}

package com.liveperson.mobilemessagingexercise.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class LpFirebaseRegistrationIntentService extends IntentService {
    public static final String TAG = LpFirebaseRegistrationIntentService.class.getSimpleName();

    public LpFirebaseRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}

package com.liveperson.mobilemessagingexercise.services;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;


public class LpFirebaseInstanceIDService extends FirebaseInstanceIdService {

	private static final String TAG = LpFirebaseInstanceIDService.class.getSimpleName();

	@Override
	public void onTokenRefresh() {
		Log.d(TAG,"New Firebase token received: ");
		Intent registrationIntent = new Intent(this, LpFirebaseRegistrationIntentService.class);

		//Start or restart the service
		startService(registrationIntent);
	}

}

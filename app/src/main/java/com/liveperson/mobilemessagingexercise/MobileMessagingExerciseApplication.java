package com.liveperson.mobilemessagingexercise;

import android.app.Application;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.liveperson.api.LivePersonCallbackImpl;
import com.liveperson.api.LivePersonIntents;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;
import com.liveperson.mobilemessagingexercise.receivers.LivePersonBroadcastReceiver;


public class MobileMessagingExerciseApplication extends Application {

    private static final String TAG = MobileMessagingExerciseApplication.class.getSimpleName();

    private static boolean showToastOnCallback = true;
    private ApplicationStorage applicationStorage;
    private LivePersonBroadcastReceiver livePersonBroadcastReceiver;
    private boolean isLoggedIn = false;
    private String jwt;

    @Override
    public void onCreate () {
        super.onCreate();
        applicationStorage = ApplicationStorage.getInstance();
        applicationStorage.setAppId("com.liveperson.mobilemessagingexercise");
        applicationStorage.setBrandServerBaseUrl("http://10.0.8.130:5000");

        //TODO - Replace with your app's LiveEngage application installation id
        applicationStorage.setAppInstallationId("2aa6228e-1edc-443a-83e9-341a8696a1d8");

        /* TODO - Replace with your LiveEngage Account Number */
        applicationStorage.setBrandAccountNumber("20553802");


        //Register the app to receive events from LivePerson
        registerForLivePersonEvents();
    }

    /**
     * Register to receive events from LivePerson within this application
     */
    private void registerForLivePersonEvents() {
        livePersonBroadcastReceiver = new LivePersonBroadcastReceiver(this);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(livePersonBroadcastReceiver, LivePersonIntents.getIntentFilterForAllEvents());
    }

    /**
     * Display a pop up toast message
     * @param message the text of the message to be shown
     */
    public void showToast(String message) {
        if (showToastOnCallback){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }else{
            Log.d(TAG + "_CALLBACK", message);
        }
    }

    /******************************************
     * Bean methods
     *****************************************/
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}

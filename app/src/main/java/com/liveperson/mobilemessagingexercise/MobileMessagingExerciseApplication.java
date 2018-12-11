package com.liveperson.mobilemessagingexercise;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.liveperson.api.LivePersonCallbackImpl;
import com.liveperson.api.LivePersonIntents;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.infra.log.LPMobileLog;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;
import com.liveperson.mobilemessagingexercise.receivers.LivePersonBroadcastReceiver;


public class MobileMessagingExerciseApplication extends Application {

    private static final String TAG = MobileMessagingExerciseApplication.class.getSimpleName();

    private boolean showToastOnCallback = true;
    private ApplicationStorage applicationStorage;
    private LivePersonCallbackImpl livePersonCallback;
    private LivePersonBroadcastReceiver livePersonBroadcastReceiver;
    private boolean livePersonInitialized = false;
    private String brandServerBaseUrl = "http://10.0.8.243:5000";
    private boolean isLoggedIn = false;
    private String jwt;

    @Override
    public void onCreate () {
        super.onCreate();
        applicationStorage = ApplicationStorage.getInstance();
        //Initialize the connection to LivePerson
        initializeLivePerson();

        //Register the app to receive events from LivePerson
        registerForLivePersonEvents();

    }

    /**
     * Initialize the connection to LivePerson for the entire application
     */
    public void initializeLivePerson() {
        InitLivePersonProperties initLivePersonProperties =
                new InitLivePersonProperties(applicationStorage.getBrandAccountNumber(),
                        applicationStorage.getAppId(),
                        new MobileMessagingInitCallback());

        LivePerson.initialize(this, initLivePersonProperties);
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
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }else{
            LPMobileLog.d(TAG + "_CALLBACK", message);
        }
    }

    /**********************************************
     * Inner classes
     *********************************************/

    /**
     * Callback associated with initialization of LivePerson
     */
    private class MobileMessagingInitCallback implements InitLivePersonCallBack {
        /**
         * Invoked if initialization of LivePerson is successful
         */
        @Override
        public void onInitSucceed() {
            Log.i(TAG, "LivePerson SDK initialize completed");
            showToast("LivePerson SDK initialize completed");
            setLivePersonInitialized(true);
        }

        /**
         * Invoked if initialization of LivePerson fails
         * @param e the exception associated with the failure
         */
        @Override
        public void onInitFailed(Exception e) {
            Log.e(TAG, "LivePerson SDK initialize failed", e);
            showToast("Unable to initialize LivePerson");
            setLivePersonInitialized(false);
        }
    }

    /******************************************
     * Bean methods
     *****************************************/
    public boolean isLivePersonInitialized() {
        return livePersonInitialized;
    }

    public void setLivePersonInitialized(boolean livePersonInitialized) {
        this.livePersonInitialized = livePersonInitialized;
    }

    public String getBrandServerBaseUrl() {
        return brandServerBaseUrl;
    }

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

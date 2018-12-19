package com.liveperson.mobilemessagingexercise;

import android.app.Application;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.liveperson.api.LivePersonIntents;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;
import com.liveperson.mobilemessagingexercise.receivers.LivePersonBroadcastReceiver;

/******************************************************************
 * The main application class for the Mobile Messaging Exercise
 *****************************************************************/
public class MobileMessagingExerciseApplication extends Application {

    private static final String TAG = MobileMessagingExerciseApplication.class.getSimpleName();
    private static final String BRAND_SERVER_URL = "http://10.0.8.141:5000";
    private static final String LIVE_PERSON_APP_ID = "com.liveperson.mobilemessagingexercise";

    //TODO - Replace with your app's LiveEngage account number
    private static final String LIVE_PERSON_APP_ACCOUNT_NUMBER = "20553802";

    //TODO - Replace with your app's LiveEngage application installation id
    private static final String LIVE_PERSON_APP_INSTALLATION_ID = "2aa6228e-1edc-443a-83e9-341a8696a1d8";

    private static boolean showToastOnCallback = true;
    private ApplicationStorage applicationStorage;
    private LivePersonBroadcastReceiver livePersonBroadcastReceiver;
    private boolean isLoggedIn = false;
    private String jwt;

    /**
     * Android callback invoked as the application is created
     */
    @Override
    public void onCreate () {
        super.onCreate();
        applicationStorage = ApplicationStorage.getInstance();
        applicationStorage.setAppId(LIVE_PERSON_APP_ID);
        applicationStorage.setBrandServerBaseUrl(BRAND_SERVER_URL);
        applicationStorage.setAppInstallationId(LIVE_PERSON_APP_INSTALLATION_ID);
        applicationStorage.setBrandAccountNumber(LIVE_PERSON_APP_ACCOUNT_NUMBER);

        //Register the app to receive events from LivePerson
        registerForLivePersonEvents();
    }

    /**
     * Register to receive events from LivePerson within this application
     */
    private void registerForLivePersonEvents() {
        //TODO Phase 1: Register for LivePerson events
    }

    /**
     * Display a pop up toast message
     * @param message the text of the message to be shown
     *
     * If showToastOnCallback is false, the message is logged instead
     */
    public void showToast(String message) {
        if (showToastOnCallback){
            //Show the message as a popup
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else {
            //Log the message
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

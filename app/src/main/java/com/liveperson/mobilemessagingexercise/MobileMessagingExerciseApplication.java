package com.liveperson.mobilemessagingexercise;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.liveperson.api.LivePersonCallbackImpl;
import com.liveperson.api.LivePersonIntents;
import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.infra.log.LPMobileLog;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.model.ConsumerProfile;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;
import com.liveperson.mobilemessagingexercise.receivers.LivePersonBroadcastReceiver;


public class MobileMessagingExerciseApplication extends Application {

    private static final String TAG = MobileMessagingExerciseApplication.class.getSimpleName();
    private static MobileMessagingExerciseApplication applicationInstance;

    private ApplicationStorage applicationStorage;
    private LivePersonCallbackImpl livePersonCallback;
    private BroadcastReceiver mLivePersonReceiver;
    private boolean showToastOnCallback = true;
    private LivePersonBroadcastReceiver livePersonBroadcastReceiver;
    private boolean livePersonInitialized = false;

    @Override
    public void onCreate () {
        super.onCreate();
        applicationInstance = this;
        applicationStorage = ApplicationStorage.getInstance();
        //Initialize the connection to LivePerson
        initializeLivePerson();

        //Register the app to receive events from LivePerson
        registerForLivePersonEvents();

    }

    /**
     * Get the singleton instance
     * @return the only instance of this class
     */
    public static MobileMessagingExerciseApplication getInstance() {
        return applicationInstance;
    }

    public void showToast(String message) {
        if (showToastOnCallback){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }else{
            LPMobileLog.d(TAG + "_CALLBACK", message);
        }
    }

    public void initializeLivePerson() {
        InitLivePersonProperties initLivePersonProperties =
                new InitLivePersonProperties(applicationStorage.getBrandAccountNumber(),
                        applicationStorage.getAppId(),
                        new MobileMessagingInitCallback());

        LivePerson.initialize(this, initLivePersonProperties);
    }

    private void registerForLivePersonEvents() {
        livePersonBroadcastReceiver = new LivePersonBroadcastReceiver(applicationInstance);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(livePersonBroadcastReceiver, LivePersonIntents.getIntentFilterForAllEvents());
    }

    /**********************************************
     * Inner classes
     *********************************************/
    private class MobileMessagingInitCallback implements InitLivePersonCallBack {
        @Override
        public void onInitSucceed() {
            Log.i(TAG, "LivePerson SDK initialize completed");
            showToast("LivePerson SDK initialize completed");
            setLivePersonInitialized(true);
        }

        @Override
        public void onInitFailed(Exception e) {
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
}

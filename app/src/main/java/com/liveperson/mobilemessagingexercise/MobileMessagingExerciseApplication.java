package com.liveperson.mobilemessagingexercise;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.widget.Toast;

import com.liveperson.api.LivePersonCallbackImpl;
import com.liveperson.api.LivePersonIntents;
import com.liveperson.api.response.types.CloseReason;
import com.liveperson.api.sdk.PermissionType;
import com.liveperson.api.sdk.LPConversationData;
import com.liveperson.infra.log.LPMobileLog;
import com.liveperson.mobilemessagingexercise.receivers.LivePersonBroadcastReceiver;


public class MobileMessagingExerciseApplication extends Application {

    private static final String TAG = MobileMessagingExerciseApplication.class.getSimpleName();
    public static MobileMessagingExerciseApplication applicationInstance;
    private LivePersonCallbackImpl livePersonCallback;
    private BroadcastReceiver mLivePersonReceiver;
    private boolean showToastOnCallback;
    private LivePersonBroadcastReceiver livePersonBroadcastReceiver;

    @Override
    public void onCreate () {
        super.onCreate();
        applicationInstance = this;
        livePersonBroadcastReceiver = new LivePersonBroadcastReceiver(applicationInstance);

        //registerForLivePersonEvents();



    }

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

}

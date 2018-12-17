package com.liveperson.mobilemessagingexercise.ActivityRunners;

import android.app.Activity;
import android.util.Log;

import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.callbacks.LogoutLivePersonCallback;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/**************************************************************************
 * Class to clear any existing conversation before running a new activity
 *************************************************************************/
public class ClearRunner implements LogoutLivePersonCallback{
    private static final String TAG = ClearRunner.class.getSimpleName();

    private Activity hostContext;
    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication applicationInstance;
    private Runnable runnable;

    /**
     * Constructor
     * @param hostContext the context of the activity that starts this instance
     */
    public ClearRunner(Activity hostContext, ApplicationStorage applicationStorage) {
        this.hostContext = hostContext;
        this.applicationStorage = applicationStorage;
        this.applicationInstance = (MobileMessagingExerciseApplication)hostContext.getApplication();
    }

    public void clearAndRun(Runnable runnable) {
        this.runnable = runnable;
        LivePerson.logOut(hostContext, applicationStorage.getBrandAccountNumber(),
                applicationStorage.getAppId(), this) ;
    }

    /**
     * Invoked if initialization of LivePerson is successful
     */
    @Override
    public void onLogoutSucceed() {
        Log.i(TAG, "LivePerson SDK logout completed");
        applicationInstance.showToast("LivePerson SDK logout completed");
        //Now run the specified activity
        hostContext.runOnUiThread(runnable);
    }

    /**
     * Invoked if initialization of LivePerson fails
     */
    @Override
    public void onLogoutFailed() {
        Log.e(TAG, "LivePerson SDK logout failed");
        applicationInstance.showToast("Unable to log out from LivePerson");
    }

}


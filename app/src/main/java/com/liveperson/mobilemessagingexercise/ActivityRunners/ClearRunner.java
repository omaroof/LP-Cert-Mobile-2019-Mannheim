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
     * @param applicationStorage the singleton holding the shared storage for the app
     */
    public ClearRunner(Activity hostContext, ApplicationStorage applicationStorage) {
        this.hostContext = hostContext;
        this.applicationStorage = applicationStorage;
        this.applicationInstance = (MobileMessagingExerciseApplication)hostContext.getApplication();
    }

    /**
     * Clear any existing conversation, and then execute the specified runnable
     * @param runnable the runnable to execute
     */
    public void clearAndRun(Runnable runnable) {
        this.runnable = runnable;
        //Log out from LivePerson, clearing any existing conversation
        LivePerson.logOut(hostContext, applicationStorage.getBrandAccountNumber(),
                applicationStorage.getAppId(), this) ;
    }

    /**
     * Invoked if logout from LivePerson is successful
     */
    @Override
    public void onLogoutSucceed() {
        Log.i(TAG, "LivePerson SDK logout completed");
        applicationInstance.showToast("LivePerson SDK logout completed");

        //Logout has been successful, so execute the runnable
        hostContext.runOnUiThread(runnable);
    }

    /**
     * Invoked if logout from LivePerson fails
     */
    @Override
    public void onLogoutFailed() {
        Log.e(TAG, "LivePerson SDK logout failed");
        applicationInstance.showToast("Unable to log out from LivePerson");
    }

}


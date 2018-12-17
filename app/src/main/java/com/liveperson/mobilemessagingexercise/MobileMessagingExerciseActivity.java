package com.liveperson.mobilemessagingexercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.liveperson.mobilemessagingexercise.ActivityRunners.ClearRunner;
import com.liveperson.mobilemessagingexercise.ConversationRunners.AskUsRunner;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;
import com.liveperson.mobilemessagingexercise.ConversationRunners.MyAccountRunner;

/******************************************************************
 * Parent class providing common capabilities for the activities
 * in the Mobile Messaging Exercise
 *****************************************************************/
public class MobileMessagingExerciseActivity extends AppCompatActivity {

    private static final String TAG = MobileMessagingExerciseActivity.class.getSimpleName();
    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication applicationInstance;
    private ClearRunner clearRunner;

    /**
     * Android callback invoked as the activity is created
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationInstance = (MobileMessagingExerciseApplication)getApplication();
        applicationStorage = ApplicationStorage.getInstance();
        clearRunner = new ClearRunner(this, applicationStorage);

        Log.i(TAG, "MobileMessagingActivity created");
    }

    /**
     * Android callback invoked as the activity is destroyed
     */
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Display a pop up toast message
     * @param message the text of the message to be shown
     */
    protected void showToast(String message) {
        //Delegate to the method in the application
        applicationInstance.showToast(message);
    }

    /**
     * Transfer control to the Welcome activity
     */
    protected void startWelcome() {
        Intent intentWelcome = new Intent(this, WelcomeActivity.class);
        this.startActivity(intentWelcome);
    }

    /**
     * Transfer control to the Login activity
     */
    protected void startLogin() {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        this.startActivity(intentLogin);
    }

    /**
     * Transfer control to the My Account activity
     */
    protected void startMyAccount() {
        MyAccountRunner myAccountRunner = new MyAccountRunner(this, applicationStorage);
        //Clear any previous conversation, and start My Account
        clearRunner.clearAndRun(myAccountRunner);
    }

    /**
     * Transfer control to the Ask Us activity
     */
    protected void startAskUs() {
        AskUsRunner askUsRunner = new AskUsRunner(this, applicationStorage);
        clearRunner.clearAndRun(askUsRunner);
    }

    protected String getBrandServerBaseUrl() {
        return applicationStorage.getBrandServerBaseUrl();
    }

    /*************************
     * Bean methods
     ************************/
    public ApplicationStorage getApplicationStorage() {
        return applicationStorage;
    }

    public MobileMessagingExerciseApplication getApplicationInstance() {
        return applicationInstance;
    }
}

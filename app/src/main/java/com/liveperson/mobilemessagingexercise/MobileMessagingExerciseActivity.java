package com.liveperson.mobilemessagingexercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.model.ConsumerProfile;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/**
 * Parent class providing common capabilities for all Activities
 */
public class MobileMessagingExerciseActivity extends AppCompatActivity {

    private static final String TAG = MobileMessagingExerciseActivity.class.getSimpleName();
    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication applicationInstance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationInstance = (MobileMessagingExerciseApplication)getApplication();
        applicationStorage = ApplicationStorage.getInstance();
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
        //TODO implement this
    }

    /**
     * Transfer control to the Ask Us activity
     */
    protected void startAskUs() {
        AskUsRunner askUsRunner = new AskUsRunner(this);
        runOnUiThread(askUsRunner);
    }

    /**********************************************
     * Inner Classes
     *********************************************/
    protected class AskUsRunner implements Runnable {
        private Activity hostContext;

        /**
         * Constructor
         *
         * @param hostContext the context of the activity that starts this instance
         */
        public AskUsRunner(Activity hostContext) {
            this.hostContext = hostContext;
        }

        @Override
        public void run() {
            LPAuthenticationParams authParams = new LPAuthenticationParams();
            authParams.setHostAppJWT(applicationStorage.getJwtPublicKey());

            ConversationViewParams conversationViewParams = new ConversationViewParams(false);
            conversationViewParams.setHistoryConversationsStateToDisplay(LPConversationsHistoryStateToDisplay.ALL);

            //Start the conversation
            LivePerson.showConversation(hostContext, authParams, conversationViewParams);

            ConsumerProfile consumerProfile = new ConsumerProfile.Builder()
                    .setFirstName(applicationStorage.getFirstName())
                    .setLastName(applicationStorage.getLastName())
                    .setPhoneNumber(applicationStorage.getPhoneNumber())
                    .build();
            LivePerson.setUserProfile(consumerProfile);
        }
    }

    /*************************
     * Bean methods
     ************************/
    public ApplicationStorage getApplicationStorage() {
        return applicationStorage;
    }

    public void setApplicationStorage(ApplicationStorage applicationStorage) {
        this.applicationStorage = applicationStorage;
    }

}

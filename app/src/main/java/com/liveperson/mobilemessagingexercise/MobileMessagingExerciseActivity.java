package com.liveperson.mobilemessagingexercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.infra.MonitoringInitParams;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.messaging.sdk.api.callbacks.LogoutLivePersonCallback;
import com.liveperson.messaging.sdk.api.model.ConsumerProfile;
import com.liveperson.mobilemessagingexercise.ConversationRunners.AskUsRunner;
import com.liveperson.mobilemessagingexercise.ConversationRunners.UnAuthenticatedConversationRunner;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/**
 * Parent class providing common capabilities for all Activities
 */
public class MobileMessagingExerciseActivity extends AppCompatActivity {

    private static final String TAG = MobileMessagingExerciseActivity.class.getSimpleName();
    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication applicationInstance;
    private boolean livePersonInitialized = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationInstance = (MobileMessagingExerciseApplication)getApplication();
        applicationStorage = ApplicationStorage.getInstance();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Clear any existing conversation before running the activity associated with
     * a new one.
     * @param runnable the runnable which executes the chosen activity
     */
    protected void clearAndRun(Runnable runnable) {
        ClearAndRunLogoutCallback clearAndRunCallback = new ClearAndRunLogoutCallback(runnable);

        LivePerson.logOut(this, applicationStorage.getBrandAccountNumber(),
                applicationStorage.getAppId(), clearAndRunCallback) ;
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
        MyAccountRunner myAccountRunner = new MyAccountRunner(this);
        //Clear any previous conversation, and start My Account
        clearAndRun(myAccountRunner);
    }

    /**
     * Transfer control to the Ask Us activity
     */
    protected void startAskUs() {
        AskUsRunner askUsRunner = new AskUsRunner(this, applicationStorage);
        clearAndRun(askUsRunner);
    }

    protected String getBrandServerBaseUrl() {
        return applicationStorage.getBrandServerBaseUrl();
    }

    /**********************************************
     * Inner Classes
     *********************************************/
    /******************************************
     * Class to run the My Account screen
     *****************************************/
    protected class MyAccountRunner implements Runnable {
        private Activity hostContext;

        /**
         * Constructor
         * @param hostContext the context of the activity that starts this instance
         */
        public MyAccountRunner(Activity hostContext) {
            this.hostContext = hostContext;
        }

        @Override
        public void run() {

            LPAuthenticationParams authParams = new LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH);
            authParams.setHostAppJWT(applicationInstance.getJwt());

            ConversationViewParams conversationViewParams = new ConversationViewParams(false);
            conversationViewParams.setHistoryConversationsStateToDisplay(LPConversationsHistoryStateToDisplay.ALL);

            AuthenticatedConversationRunner authenticatedConversationRunner =
                new AuthenticatedConversationRunner(this.hostContext, authParams, conversationViewParams);

            InitLivePersonProperties initLivePersonProperties =
                    new InitLivePersonProperties(applicationStorage.getBrandAccountNumber(),
                            applicationStorage.getAppId(),
                            null,
                            authenticatedConversationRunner);


            LivePerson.initialize(this.hostContext, initLivePersonProperties);

        }
    }

    /****************************************************************
     * Class to initiate an authenticated conversation in response to
     * successful initialization of LivePerson
     ***************************************************************/
    protected class AuthenticatedConversationRunner implements InitLivePersonCallBack {

        private Activity hostContext;
        private LPAuthenticationParams authParams;
        private ConversationViewParams conversationViewParams;
        private ConsumerProfile consumerProfile;

        /**
         * Construct the conversation runner
         * @param hostContext the context from which the conversation is to be run
         * @param authParams the LivePerson authentication parameters for the conversation
         * @param conversationViewParams the LivePerson view parameters for the conversation
         */
        public AuthenticatedConversationRunner(Activity hostContext, LPAuthenticationParams authParams, ConversationViewParams conversationViewParams) {
            this.hostContext = hostContext;
            this.authParams = authParams;
            this.conversationViewParams = conversationViewParams;
        }

        /**
         * Invoked if initialization of LivePerson is successful
         */
        @Override
        public void onInitSucceed() {
            //Display and log a confirmation message
            Log.i(TAG, "LivePerson SDK initialize completed");
            showToast("LivePerson SDK initialize completed");
            setLivePersonInitialized(true);

            //Start the conversation
            LivePerson.showConversation(hostContext, authParams, conversationViewParams);
        }

        /**
         * Invoked if initialization of LivePerson fails
         * @param e the exception associated with the failure
         */
        @Override
        public void onInitFailed(Exception e) {
            //Display and log the error
            Log.e(TAG, "LivePerson SDK initialize failed", e);
            showToast("Unable to initialize LivePerson");
            setLivePersonInitialized(false);
        }
    }

    /****************************************************************
     * Class to handle callbacks from Logout from LivePerson
     ***************************************************************/
    protected class ClearAndRunLogoutCallback implements LogoutLivePersonCallback {
        Runnable activity;

        protected ClearAndRunLogoutCallback(Runnable activity) {
            this.activity = activity;
        }

        /**
         * Invoked if initialization of LivePerson is successful
         */
        @Override
        public void onLogoutSucceed() {
            Log.i(TAG, "LivePerson SDK logout completed");
            showToast("LivePerson SDK logout completed");
            setLivePersonInitialized(false);
            //Now run the specified activity
            runOnUiThread(activity);
        }

        /**
         * Invoked if initialization of LivePerson fails
         */
        @Override
        public void onLogoutFailed() {
            Log.e(TAG, "LivePerson SDK logout failed");
            showToast("Unable to log out from LivePerson");
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

    public MobileMessagingExerciseApplication getApplicationInstance() {
        return applicationInstance;
    }

    public boolean isLivePersonInitialized() {
        return livePersonInitialized;
    }

    public void setLivePersonInitialized(boolean livePersonInitialized) {
        this.livePersonInitialized = livePersonInitialized;
    }

}

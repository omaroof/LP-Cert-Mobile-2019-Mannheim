package com.liveperson.mobilemessagingexercise.Conversations;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.infra.messaging_ui.fragment.ConversationFragment;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.Fragments.MyAccountFragment;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.R;
import com.liveperson.mobilemessagingexercise.model.ApplicationConstants;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;
import com.liveperson.mobilemessagingexercise.services.LpFirebaseRegistrationIntentService;

/**************************************************************************************
 * Class to display the My Account Screen.
 * Provides the LivePerson initialization callback
 *************************************************************************************/
public class MyAccountFragmentConversation implements Runnable, InitLivePersonCallBack {
    private static final String TAG = MyAccountFragmentConversation.class.getSimpleName();

    private static final String LIVEPERSON_FRAGMENT = "liveperson_fragment";

    private MyAccountFragment myAccountFragment;
    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication applicationInstance;
    private LPAuthenticationParams authParams;
    private ConversationViewParams conversationViewParams;
    private ConversationFragment lpConversationFragment;

    /**
     * Convenience constructor
     * @param myAccountFragment the fragment container in which this conversation is to run
     * @param applicationStorage the singleton holding the shared storage for the app
     */
    public MyAccountFragmentConversation(MyAccountFragment myAccountFragment, ApplicationStorage applicationStorage) {
        this.myAccountFragment = myAccountFragment;
        this.applicationStorage = applicationStorage;
        this.applicationInstance = (MobileMessagingExerciseApplication)myAccountFragment.getApplication();
    }

    /**
     * Run the My Account screen as a LivePerson conversation
     */
    @Override
    public void run() {
        //Set up the parameters needed for initializing LivePerson
        InitLivePersonProperties initLivePersonProperties =
                new InitLivePersonProperties(ApplicationConstants.LIVE_PERSON_ACCOUNT_NUMBER,
                        ApplicationConstants.LIVE_PERSON_APP_ID,
                        null,
                        this);

        //Initialize LivePerson for the My Account screen
        LivePerson.initialize(this.myAccountFragment, initLivePersonProperties);
    }

    /**
     * Set up and show the LivePerson conversation associated with the My Account screen
     * Invoked if initialization of LivePerson is successful
     */
    @Override
    public void onInitSucceed() {
        //Display and log a confirmation message
        Log.i(TAG, "LivePerson SDK initialize completed");
        showToast("LivePerson SDK initialize completed");

        //Set up the authentication parameters
        authParams = new LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH);
        authParams.setAuthKey("");
        authParams.addCertificatePinningKey("");
        authParams.setHostAppJWT(applicationStorage.getJwt());

        //Set up the conversation view parameters
        conversationViewParams = new ConversationViewParams(false);
        conversationViewParams.setHistoryConversationsStateToDisplay(LPConversationsHistoryStateToDisplay.ALL);

        //Create the LivePerson fragment
        lpConversationFragment = (ConversationFragment) LivePerson.getConversationFragment(authParams, conversationViewParams);

        if (isValidState(myAccountFragment)) {
            FragmentTransaction ft = myAccountFragment.getSupportFragmentManager().beginTransaction();
            ft.add(R.id.my_account_fragment_container, lpConversationFragment, LIVEPERSON_FRAGMENT).commitAllowingStateLoss();

        }

        //Start or restart the registration intent service
        Intent registrationIntent = new Intent(myAccountFragment, LpFirebaseRegistrationIntentService.class);
        myAccountFragment.startService(registrationIntent);

    }

    /**
     * Report an initialization error
     * Invoked if initialization of LivePerson fails
     * @param e the exception associated with the failure
     */
    @Override
    public void onInitFailed(Exception e) {
        //Display and log the error
        Log.e(TAG, "LivePerson SDK initialize failed", e);
        showToast("Unable to initialize LivePerson");
    }

    /**
     * Check that we are in a suitable state
     * @return true if the state is suitable and false otherwise
     */
    private boolean isValidState(FragmentActivity fragmentActivity) {
        return !fragmentActivity.isFinishing() && !fragmentActivity.isDestroyed();
    }

    /**
     * Convenience method to display a pop up toast message from any activity
     * @param message the text of the message to be shown
     */
    private void showToast(String message) {
        //Delegate to the method in the application
        applicationInstance.showToast(message);
    }
}


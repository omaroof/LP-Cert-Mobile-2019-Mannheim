package com.liveperson.mobilemessagingexercise.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.infra.messaging_ui.fragment.ConversationFragment;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.callbacks.LogoutLivePersonCallback;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseActivity;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.R;
import com.liveperson.mobilemessagingexercise.model.ApplicationConstants;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;
import com.liveperson.mobilemessagingexercise.services.LpFirebaseRegistrationIntentService;

/**************************************************************************************
 * Class to display the My Account Screen.
 * Provides the LivePerson initialization callback
 *************************************************************************************/
public class MyAccountFragment extends MobileMessagingExerciseActivity {
    private static final String TAG = MyAccountFragment.class.getSimpleName();

    private static final String LIVEPERSON_FRAGMENT = "liveperson_fragment";

    private LPAuthenticationParams authParams;
    private ConversationViewParams conversationViewParams;
    private LpFragmentInitializer lpFragmentInitializer;
    private ConversationFragment lpConversationFragment;

    /**
     * Android callback invoked as the activity is created
     * @param savedInstanceState any instance state data saved in a previous execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acount);

        lpFragmentInitializer = new LpFragmentInitializer();
        //Initialize the fragment used by LivePerson
        runOnUiThread(lpFragmentInitializer);
    }

    /**
     * Convenience method to display a pop up toast message from any activity
     * @param message the text of the message to be shown
     */
    protected void showToast(String message) {
        //Delegate to the method in the application
        getApplicationInstance().showToast(message);
    }

    /**
     * Class to initialize the fragment to contain the conversation with LiveEngage
     */
    private class LpFragmentInitializer implements Runnable, LogoutLivePersonCallback, InitLivePersonCallBack {

        @Override
        public void run() {
            //Log out from LivePerson, clearing any existing conversation
            LivePerson.logOut(getApplicationContext(), ApplicationConstants.LIVE_PERSON_ACCOUNT_NUMBER,
                    ApplicationConstants.LIVE_PERSON_APP_ID, this);
        }

        @Override
        public void onLogoutSucceed() {
            Log.i(TAG, "LivePerson SDK logout completed");
            showToast("LivePerson SDK logout completed");

            //Set up the parameters needed for initializing LivePerson
            InitLivePersonProperties initLivePersonProperties =
                    new InitLivePersonProperties(ApplicationConstants.LIVE_PERSON_ACCOUNT_NUMBER,
                            ApplicationConstants.LIVE_PERSON_APP_ID,
                            null,
                            this);

            //Initialize LivePerson for the My Account screen
            LivePerson.initialize(getApplicationContext(), initLivePersonProperties);
        }

        /**
         * Report the failure to log out from LivePerson
         * Invoked if logout from LivePerson fails
         */
        @Override
        public void onLogoutFailed() {
            Log.e(TAG, "LivePerson SDK logout failed");
            showToast("Unable to log out from LivePerson");
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
            authParams.setHostAppJWT(getApplicationStorage().getJwt());

            //Set up the conversation view parameters
            conversationViewParams = new ConversationViewParams(false);
            conversationViewParams.setHistoryConversationsStateToDisplay(LPConversationsHistoryStateToDisplay.ALL);

            //Create the LivePerson fragment
            lpConversationFragment = (ConversationFragment) LivePerson.getConversationFragment(authParams, conversationViewParams);

            if (isValidState()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.my_account_fragment_container, lpConversationFragment, LIVEPERSON_FRAGMENT).commitAllowingStateLoss();

            }

            //Start or restart the registration intent service
            Intent registrationIntent = new Intent(getApplicationContext(), LpFirebaseRegistrationIntentService.class);
            getApplicationContext().startService(registrationIntent);
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
         * Attach an existing fragment if suitable
         */
        private void attachExistingFragment() {
            Log.d(TAG, "Attaching existing LP conversation fragment");
            showToast("Attaching existing LP conversation fragment");
            if (isValidState()) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.attach(lpConversationFragment).commitAllowingStateLoss();

            }
        }

        /**
         * Check that we are in a suitable state
         * @return true if the state is suitable and false otherwise
         */
        private boolean isValidState() {
            return !isFinishing() && !isDestroyed();
        }

    }

}


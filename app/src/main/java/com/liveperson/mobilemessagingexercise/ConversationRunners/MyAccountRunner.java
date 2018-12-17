package com.liveperson.mobilemessagingexercise.ConversationRunners;

import android.app.Activity;
import android.util.Log;

import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/**************************************************************************************
 * Class to run the My Account Screen. Provides the LivePerson initialization callback
 *************************************************************************************/
public class MyAccountRunner implements Runnable, InitLivePersonCallBack {
    private static final String TAG = MyAccountRunner.class.getSimpleName();

    private Activity hostContext;
    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication applicationInstance;

    /**
     * Constructor
     * @param hostContext the context of the activity that starts this instance
     */
    public MyAccountRunner(Activity hostContext, ApplicationStorage applicationStorage) {
        this.hostContext = hostContext;
        this.applicationStorage = applicationStorage;
        this.applicationInstance = (MobileMessagingExerciseApplication)hostContext.getApplication();
    }

    /**
     * Start the My Account screen
     */
    @Override
    public void run() {
        //Set up the parameters needed for initializing LivePerson
        InitLivePersonProperties initLivePersonProperties =
                new InitLivePersonProperties(applicationStorage.getBrandAccountNumber(),
                        applicationStorage.getAppId(),
                        null,
                        this);

        //Initialize LivePerson for the My Account screen
        LivePerson.initialize(this.hostContext, initLivePersonProperties);
    }

    /**
     * Invoked if initialization of LivePerson is successful
     */
    @Override
    public void onInitSucceed() {
        //Display and log a confirmation message
        Log.i(TAG, "LivePerson SDK initialize completed");
        applicationInstance.showToast("LivePerson SDK initialize completed");

        //Set up the authentication parameters
        LPAuthenticationParams authParams = new LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH);
        authParams.setAuthKey("");
        authParams.addCertificatePinningKey("");
        authParams.setHostAppJWT(applicationInstance.getJwt());

        //Set up the conversation view parameters
        ConversationViewParams conversationViewParams = new ConversationViewParams(false);
        conversationViewParams.setHistoryConversationsStateToDisplay(LPConversationsHistoryStateToDisplay.ALL);

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
        applicationInstance.showToast("Unable to initialize LivePerson");
    }
}


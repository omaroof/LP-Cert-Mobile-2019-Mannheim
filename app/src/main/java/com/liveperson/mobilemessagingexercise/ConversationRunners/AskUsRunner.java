package com.liveperson.mobilemessagingexercise.ConversationRunners;

import android.app.Activity;
import android.util.Log;
import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.model.ConsumerProfile;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/***********************************************************************************
 * Class to run the Ask Us Screen. Provides the LivePerson initialization callback
 **********************************************************************************/
public class AskUsRunner implements Runnable, InitLivePersonCallBack {
    private static final String TAG = Runnable.class.getSimpleName();

    private Activity hostContext;
    private ApplicationStorage applicationStorage;
    private ConversationViewParams conversationViewParams;
    private ConsumerProfile consumerProfile;
    private MobileMessagingExerciseApplication applicationInstance;

    /**
     * Convenience constructor
     * @param hostContext the context of the activity in which the screen is to run
     * @param applicationStorage the singleton holding the shared storage for the app
     */
    public AskUsRunner(Activity hostContext, ApplicationStorage applicationStorage) {
        this.hostContext = hostContext;
        this.applicationStorage = applicationStorage;
        this.applicationInstance = (MobileMessagingExerciseApplication)hostContext.getApplication();
    }

    /**
     * Run the Ask Us screen as a LivePerson conversation
     */
    @Override
    public void run() {
        //Set up the parameters to initialize LivePerson for messaging
        InitLivePersonProperties initLivePersonProperties =
                new InitLivePersonProperties(applicationStorage.getBrandAccountNumber(),
                        applicationStorage.getAppId(),
                        null,
                        this);

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

        //Set up the consumer profile
        this.consumerProfile = new ConsumerProfile.Builder()
             .setFirstName(applicationStorage.getFirstName())
             .setLastName(applicationStorage.getLastName())
             .setPhoneNumber(applicationStorage.getPhoneNumber())
             .build();

        LivePerson.setUserProfile(consumerProfile);

        //Set up the authentication parameters
        LPAuthenticationParams authParams = new LPAuthenticationParams();
        authParams.setAuthKey("");
        authParams.addCertificatePinningKey("");

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



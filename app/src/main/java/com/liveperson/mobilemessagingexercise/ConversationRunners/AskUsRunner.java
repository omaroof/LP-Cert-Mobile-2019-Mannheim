package com.liveperson.mobilemessagingexercise.ConversationRunners;

import android.app.Activity;

import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.infra.MonitoringInitParams;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.model.ConsumerProfile;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/******************************************
 * Class to run the Ask Us screen
 *****************************************/
public class AskUsRunner implements Runnable {
    private Activity hostContext;
    private MobileMessagingExerciseApplication applicationInstance;
    private ApplicationStorage applicationStorage;

    /**
     * Constructor
     *
     * @param hostContext the context of the activity that starts this instance
     */
    public AskUsRunner(Activity hostContext, ApplicationStorage applicationStorage) {
        this.hostContext = hostContext;
        this.applicationInstance = (MobileMessagingExerciseApplication)hostContext.getApplication();
        this.applicationStorage = applicationStorage;
    }

    @Override
    public void run() {

        ConsumerProfile consumerProfile = new ConsumerProfile.Builder()
                .setFirstName(applicationStorage.getFirstName())
                .setLastName(applicationStorage.getLastName())
                .setPhoneNumber(applicationStorage.getPhoneNumber())
                .build();

        LPAuthenticationParams authParams = new LPAuthenticationParams();
        //Ask Us provides a conversation without authentication
        authParams.setAuthKey("");
        authParams.addCertificatePinningKey("");

        ConversationViewParams conversationViewParams = new ConversationViewParams(false);
        conversationViewParams.setHistoryConversationsStateToDisplay(LPConversationsHistoryStateToDisplay.ALL);

        UnAuthenticatedConversationRunner askUsRunnerStarter =
                new UnAuthenticatedConversationRunner(this.hostContext, authParams, conversationViewParams, consumerProfile);

        MonitoringInitParams monitoringInitParams = new MonitoringInitParams(applicationStorage.getAppInstallationId());

        InitLivePersonProperties initLivePersonProperties =
                new InitLivePersonProperties(applicationStorage.getBrandAccountNumber(),
                        applicationStorage.getAppId(),
                        null,
                        askUsRunnerStarter);

        LivePerson.initialize(this.hostContext, initLivePersonProperties);

    }
}



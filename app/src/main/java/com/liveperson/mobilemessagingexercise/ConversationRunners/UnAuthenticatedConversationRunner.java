package com.liveperson.mobilemessagingexercise.ConversationRunners;

import android.app.Activity;
import android.util.Log;

import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.model.ConsumerProfile;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/****************************************************************
 * Class to initiate an un-authenticated conversation in response
 * to successful initialization of LivePerson
 ***************************************************************/
public class UnAuthenticatedConversationRunner implements InitLivePersonCallBack {
    private static final String TAG = UnAuthenticatedConversationRunner.class.getSimpleName();

    private MobileMessagingExerciseApplication applicationInstance;
    private ApplicationStorage applicationStorage;
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
    public UnAuthenticatedConversationRunner(Activity hostContext, LPAuthenticationParams authParams,
                         ConversationViewParams conversationViewParams, ConsumerProfile consumerProfile) {

        this.applicationInstance = (MobileMessagingExerciseApplication)hostContext.getApplication();
        this.applicationStorage = ApplicationStorage.getInstance();
        this.hostContext = hostContext;
        this.authParams = authParams;
        this.conversationViewParams = conversationViewParams;
        this.consumerProfile = consumerProfile;
    }

    /**
     * Invoked if initialization of LivePerson is successful
     */
    @Override
    public void onInitSucceed() {
        //Display and log a confirmation message
        Log.i(TAG, "LivePerson SDK initialize completed");
        applicationInstance.showToast("LivePerson SDK initialize completed");

        //Set the consumer profile
        LivePerson.setUserProfile(consumerProfile);

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




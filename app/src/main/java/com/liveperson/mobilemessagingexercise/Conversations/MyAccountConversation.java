package com.liveperson.mobilemessagingexercise.Conversations;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.ICallback;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.LPConversationsHistoryStateToDisplay;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.model.ApplicationConstants;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/**************************************************************************************
 * Class to display the My Account Screen.
 * Provides the LivePerson initialization callback
 *************************************************************************************/
public class MyAccountConversation implements Runnable, InitLivePersonCallBack,
        OnCompleteListener<InstanceIdResult>, ICallback<Void, Exception> {
    private static final String TAG = MyAccountConversation.class.getSimpleName();

    private Activity hostContext;
    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication applicationInstance;
    private LPAuthenticationParams authParams;
    private ConversationViewParams conversationViewParams;

    /**
     * Convenience constructor
     * @param hostContext the context of the activity in which the screen is to run
     * @param applicationStorage the singleton holding the shared storage for the app
     */
    public MyAccountConversation(Activity hostContext, ApplicationStorage applicationStorage) {
        this.hostContext = hostContext;
        this.applicationStorage = applicationStorage;
        this.applicationInstance = (MobileMessagingExerciseApplication)hostContext.getApplication();
    }

    /**
     * Run the My Account screen as a LivePerson conversation
     */
    @Override
    public void run() {
        //Set up the parameters needed for initializing LivePerson
        InitLivePersonProperties initLivePersonProperties =
                new InitLivePersonProperties(ApplicationConstants.getLivePersonAccountNumber(),
                        ApplicationConstants.getLivePersonAppId(),
                        null,
                        this);

        //Initialize LivePerson for the My Account screen
        LivePerson.initialize(this.hostContext, initLivePersonProperties);
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

        //Retrieve the Firebase token to use
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(this);

        //Start the conversation
        LivePerson.showConversation(hostContext, authParams, conversationViewParams);

    }

    /**
     * Process the result of retrieving the FCM token for this app
     * @param task the task whose completion triggered this method being called
     */
    @Override
    public void onComplete(Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
            Log.w(TAG, "getInstanceId failed", task.getException());
            return;
        }

        // Get new Instance ID token
        String fcmToken = task.getResult().getToken();

        // Log and toast the token value
        Log.d(TAG, fcmToken);
        showToast(fcmToken);

        //Register to receive push messages with the new token
        LivePerson.registerLPPusher(ApplicationConstants.getLivePersonAccountNumber(), ApplicationConstants.getLivePersonAppId(),
                fcmToken, authParams, this);
    }

    @Override
    public void onSuccess(Void aVoid) {
        Log.d(TAG, "Registered for push notifications");
    }

    public void onError(Exception e) {
        Log.d(TAG, "Unable to register for push notifications");
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
     * Convenience method to display a pop up toast message from any activity
     * @param message the text of the message to be shown
     */
    protected void showToast(String message) {
        //Delegate to the method in the application
        applicationInstance.showToast(message);
    }
}


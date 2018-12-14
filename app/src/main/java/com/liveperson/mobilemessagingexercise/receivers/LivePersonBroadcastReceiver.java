package com.liveperson.mobilemessagingexercise.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.liveperson.api.LivePersonIntents;
import com.liveperson.api.sdk.LPConversationData;
import com.liveperson.api.sdk.PermissionType;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.messaging.TaskType;
import com.liveperson.messaging.model.AgentData;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.mobilemessagingexercise.MobileMessagingExerciseApplication;
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

public class LivePersonBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = LivePersonBroadcastReceiver.class.getSimpleName();
    private MobileMessagingExerciseApplication applicationInstance;

    /**
     * Constuctor
     * @param applicationInstance the instance of the application for which this is the receiver
     */
    public LivePersonBroadcastReceiver(MobileMessagingExerciseApplication applicationInstance) {
        this.applicationInstance = applicationInstance;
    }


    @Override
    public void onReceive (Context context, Intent intent) {
        Log.d(TAG, "Got LP intent event with action " + intent.getAction());

        switch (intent.getAction()) {
            case LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_AVATAR_TAPPED_INTENT_ACTION:
                onAgentAvatarTapped(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_DETAILS_CHANGED_INTENT_ACTION:
                onAgentDetailsChanged(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_TYPING_INTENT_ACTION:
                onAgentTyping(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CONNECTION_CHANGED_INTENT_ACTION:
                onConnectionChanged(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_MARKED_AS_NORMAL_INTENT_ACTION:
                onConversationMarkedAsNormal();
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_MARKED_AS_URGENT_INTENT_ACTION:
                onConversationMarkedAsUrgent();
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_RESOLVED_INTENT_ACTION:
                 onConversationResolved(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_STARTED_INTENT_ACTION:
                onConversationStarted(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_LAUNCHED_INTENT_ACTION:
                onCsatLaunched(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_DISMISSED_INTENT_ACTION:
                onCsatDismissed(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_SKIPPED_INTENT_ACTION:
                onCsatSkipped(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_SUBMITTED_INTENT_ACTION:
                onCsatSubmitted(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_ERROR_INTENT_ACTION:
                onError(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_OFFLINE_HOURS_CHANGES_INTENT_ACTION:
                onOfflineHoursChanges(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_TOKEN_EXPIRED_INTENT_ACTION:
                onTokenExpired(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_USER_DENIED_PERMISSION:
                onUserDeniedPermission(intent);
            break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_USER_ACTION_ON_PREVENTED_PERMISSION:
                onUserActionOnPreventedPermission(intent);
                break;

            case LivePersonIntents.ILivePersonIntentAction.LP_ON_STRUCTURED_CONTENT_LINK_CLICKED:
                onStructuredContentLinkClicked(intent);
                break;

        }

    }

    private void onAgentAvatarTapped(Intent intent) {
        AgentData agentData = LivePersonIntents.getAgentData(intent);
        applicationInstance.showToast("Agent Avatar Tapped: " + agentData.mFirstName +
                                      " " + agentData.mLastName);
    }

    private void onAgentDetailsChanged(Intent intent) {
        AgentData agentData = LivePersonIntents.getAgentData(intent);
        applicationInstance.showToast("Agent Details Changed: " + agentData);
    }

    private void onAgentTyping(Intent intent) {
        boolean isTyping = LivePersonIntents.getAgentTypingValue(intent);
        applicationInstance.showToast("Agent is typing: " + isTyping);
    }

    private void onConnectionChanged(Intent intent) {
        boolean isConnected = LivePersonIntents.getConnectedValue(intent);
        applicationInstance.showToast("Connected to LiveEngage: " + isConnected);
    }

    private void onConversationMarkedAsNormal() {
        applicationInstance.showToast("Conversation Marked As Normal");
    }

    private void onConversationMarkedAsUrgent() {
        applicationInstance.showToast("Conversation Marked As Urgent");
    }

    private void onConversationResolved(Intent intent) {
        LPConversationData conversationData = LivePersonIntents.getLPConversationData(intent);
        applicationInstance.showToast("Conversation started " + conversationData.getId() +
                " reason " + conversationData.getCloseReason());
    }

    private void onConversationStarted(Intent intent) {
        LPConversationData conversationData = LivePersonIntents.getLPConversationData(intent);
        applicationInstance.showToast("Conversation started " + conversationData.getId() +
                " reason " + conversationData.getCloseReason());
    }

    private void onCsatLaunched(Intent intent) {
        applicationInstance.showToast("CSAT launched");
    }

    private void onCsatDismissed(Intent intent) {
        applicationInstance.showToast("CSAT skipped");
    }

    private void onCsatSkipped(Intent intent) {
        applicationInstance.showToast("CSAT skipped");
    }

    private void onCsatSubmitted(Intent intent) {
        String conversationId = LivePersonIntents.getConversationID(intent);
        applicationInstance.showToast("CSAT submitted for conversation: " + conversationId);
    }

    private void onError(Intent intent) {
        TaskType type = LivePersonIntents.getOnErrorTaskType(intent);
        String message = LivePersonIntents.getOnErrorMessage(intent);
        applicationInstance.showToast("Error: " + type.name() + " " + message);
    }

    private void onOfflineHoursChanges(Intent intent) {
        boolean isOfflineHoursOn = LivePersonIntents.getOfflineHoursOn(intent);
        applicationInstance.showToast("Offline hours changes: " + isOfflineHoursOn);
    }

    //TODO check whether this is only for OAuth token, and not JWT
    private void onTokenExpired(Intent intent) {
        applicationInstance.showToast("Token Expired");
        //LivePerson.reconnect(new LPAuthenticationParams().setAuthKey(ApplicationStorage.getInstance().getAuthCode()));

    }

    private void onUserDeniedPermission(Intent intent) {
        PermissionType permissionType = LivePersonIntents.getPermissionType(intent);
        boolean doNotShowAgainMarked = LivePersonIntents.getPermissionDoNotShowAgainMarked(intent);
        applicationInstance.showToast("User Denied Permission: " + permissionType.name() +
                " doNotShowAgainMarked = " + doNotShowAgainMarked);
    }

    private void onUserActionOnPreventedPermission(Intent intent) {
        PermissionType permissionType = LivePersonIntents.getPermissionType(intent);
        applicationInstance.showToast("User Action On Prevented Permission: " + permissionType.name());
    }

    private void onStructuredContentLinkClicked(Intent intent) {
        String uri = LivePersonIntents.getLinkUri(intent);
        applicationInstance.showToast("Structured Content Link Clicked. Uri: " + uri);
    }


}

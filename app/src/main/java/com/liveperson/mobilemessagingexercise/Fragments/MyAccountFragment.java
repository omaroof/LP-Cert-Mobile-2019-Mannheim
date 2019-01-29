package com.liveperson.mobilemessagingexercise.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.liveperson.mobilemessagingexercise.ActivityRunners.ClearRunner;
import com.liveperson.mobilemessagingexercise.Conversations.MyAccountFragmentConversation;
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

    private MyAccountFragmentConversation myAccountFragmentConversation;

    /**
     * Android callback invoked as the activity is created
     * @param savedInstanceState any instance state data saved in a previous execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acount);
        myAccountFragmentConversation = new MyAccountFragmentConversation(this, getApplicationStorage());
        //Run the fragment conversation
        getClearRunner().clearAndRun(myAccountFragmentConversation);

    }


}


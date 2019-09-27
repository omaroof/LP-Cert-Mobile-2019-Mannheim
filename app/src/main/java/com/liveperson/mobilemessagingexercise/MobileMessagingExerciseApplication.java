package com.liveperson.mobilemessagingexercise;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.liveperson.api.LivePersonIntents;


/******************************************************************
 * The main application class for the Mobile Messaging Exercise
 *****************************************************************/
public class MobileMessagingExerciseApplication extends Application {



    /**
     * Android callback invoked as the application is created
     */
    @Override
    public void onCreate () {
        super.onCreate();

        //Register the app to receive events from LivePerson
        registerForLivePersonEvents();
    }

    /**
     * Register to receive events from LivePerson within this application
     */
    private void registerForLivePersonEvents() {

    }

    /**
     * Display a pop up toast message
     * @param message the text of the message to be shown
     *
     * If showToastOnCallback is false, the message is logged instead
     */
    public void showToast(String message) {

    }

}

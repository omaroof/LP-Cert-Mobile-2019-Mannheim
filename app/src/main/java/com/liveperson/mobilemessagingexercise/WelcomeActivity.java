package com.liveperson.mobilemessagingexercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;



/******************************************************************************
 * Class for the activity associated with the application Welcome screen
 * NOTE: This class also provides the listener for click events on the screen
 *****************************************************************************/
public class WelcomeActivity extends Activity implements View.OnClickListener {
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    /**
     * Android callback invoked as the activity is created
     * @param savedInstanceState any instance state data saved in a previous execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    /**
     * Android callback invoked as the activity is re-started by a new intent
     * @param intent the intent associated with the restart action
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processLePushMessage(intent);
    }

    /**
     * Android callback invoked as the activity is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        //Load saved data into the controls on this screen

    }

    /**
     * Android callback invoked as the options menu is created
     * @param menu the options menu in the toolbar
     * @returns true, if the menu is to be displayed, and false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add the appropriate menu items to the toolbar menu
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        //Ensure the menu is displayed
        return true;
    }


    /**
     * Handle click events for controls on the Welcome screen
     * @param view the control on which the event occurred
     */
    public void onClick(View view) {

    }

    /**
     * Process a creation or restart triggered by a push message
     * @param intent the intent associated with the push message
     */
    private void processLePushMessage(Intent intent) {

    }

}

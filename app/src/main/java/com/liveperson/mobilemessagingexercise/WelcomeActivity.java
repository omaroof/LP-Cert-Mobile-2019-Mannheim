package com.liveperson.mobilemessagingexercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/**
 * Activity associated with the application Welcome screen
 */
public class WelcomeActivity extends MobileMessagingExerciseActivity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Button askUsButton = findViewById(R.id.ask_us_button);
        askUsButton.setOnClickListener(new AskUsOnClickListener());
        Button myAccountButton = findViewById(R.id.my_account_button);
        myAccountButton.setOnClickListener(new MyAccountOnClickListener());

    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationStorage applicationStorage = getApplicationStorage();
        EditText firstNameControl = findViewById(R.id.firstName);
        firstNameControl.setText(applicationStorage.getFirstName());
        EditText lastNameControl = findViewById(R.id.lastName);
        lastNameControl.setText(applicationStorage.getLastName());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_my_account:
                if (getApplicationStorage().isLoggedIn()) {
                    //User already logged in, so go straight there
                    startMyAccount();
                }
                else {
                    //Not logged in, so need to do that first
                    startLogin();
                }
                break;
            case R.id.action_ask_us:
                startAskUs();
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**********************************************
     * Inner Classes
     *********************************************/
    private class AskUsOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText firstNameControl = findViewById(R.id.firstName);
            EditText lastNameControl = findViewById(R.id.lastName);
            getApplicationStorage().setFirstName(firstNameControl.getText().toString());
            getApplicationStorage().setLastName(lastNameControl.getText().toString());
            startAskUs();
        }
    }

    private class MyAccountOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            startLogin();
        }
    }
}

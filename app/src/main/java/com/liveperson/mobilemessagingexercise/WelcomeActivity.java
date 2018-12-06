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

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private ApplicationStorage applicationStorage;
    private MobileMessagingExerciseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationStorage = ApplicationStorage.getInstance();
        application = MobileMessagingExerciseApplication.getInstance();
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Button askUsButton = findViewById(R.id.ask_us_button);
        askUsButton.setOnClickListener(new AskUsOnClickListener());
        Button myAccountButton = findViewById(R.id.my_account_button);
        myAccountButton.setOnClickListener(new MyAccountOnClickListener());
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
                if (applicationStorage.isLoggedIn()) {
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

    private void startAskUs() {
        EditText firstNameControl = findViewById(R.id.firstName);
        EditText lastNameControl = findViewById(R.id.lastName);
        applicationStorage.setFirstName(firstNameControl.getText().toString());
        applicationStorage.setLastName(lastNameControl.getText().toString());
        MobileMessagingExerciseApplication.AskUsRunner askUsRunner = application.new AskUsRunner(this);
        runOnUiThread(askUsRunner);
    }

    private void startLogin() {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        this.startActivity(intentLogin);
    }

    private void startMyAccount() {
        //TODO implement this
    }

    /**********************************************
     * Inner Classes
     *********************************************/
    private class AskUsOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            startAskUs();
        }
    }

    private class MyAccountOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            startLogin();
        }
    }
}

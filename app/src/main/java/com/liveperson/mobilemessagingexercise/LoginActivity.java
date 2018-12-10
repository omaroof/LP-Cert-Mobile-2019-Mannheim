package com.liveperson.mobilemessagingexercise;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;

/**
 * Activity associated with the application LoginFred screen
 */
public class LoginActivity extends MobileMessagingExerciseActivity {

    ApplicationStorage applicationStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginOnClickListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ask_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.welcome:
                startWelcome();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * Log the user in
     * @param userId the user id to be used for login
     * @param password the password to be used for login
     * @return true if the login was successful, and false otherwise
     */
    private boolean logUserIn(String userId, String password) {
        boolean result = false;
        return result;
    }

    /**********************************************
     * Inner Classes
     *********************************************/
    private class LoginOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText userIdControl = findViewById(R.id.userId);
            EditText passwordControl = findViewById(R.id.password);
            boolean loginSuccessful = logUserIn(userIdControl.getText().toString(), passwordControl.getText().toString());
            if (loginSuccessful) {
                startMyAccount();
            }
            else {
                showToast("Unable to log in");
                startWelcome();
            }
        }
    }


}

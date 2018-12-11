package com.liveperson.mobilemessagingexercise;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Activity associated with the application LoginFred screen
 */
public class LoginActivity extends MobileMessagingExerciseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

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
    private void logUserIn(String userId, String password) {
        RequestQueue authenticationQueue = Volley.newRequestQueue(this);
        AuthenticationResponseListener authenticationResponseListener = new AuthenticationResponseListener();
        JSONObject credentials = new JSONObject();
        try {
            //Set up the body for the POST request to the authentication API
            credentials.put("userId", userId);
            credentials.put("password", password);
            //Create the authentication POST request
            JsonObjectRequest authenticationRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    getBrandServerBaseUrl() + "/authenticate",
                    credentials,
                    authenticationResponseListener,
                    authenticationResponseListener
            );
            //Send the authentication POST request
            authenticationQueue.add(authenticationRequest);
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage());
            showToast(e.getMessage());
        }

        return;
    }

    /**********************************************
     * Inner Classes
     *********************************************/

    /**
     * Listener for the user requesting login
     */
    private class LoginOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText userIdControl = findViewById(R.id.userId);
            EditText passwordControl = findViewById(R.id.password);
            logUserIn(userIdControl.getText().toString(), passwordControl.getText().toString());
        }
    }

    /**
     * Listener for responses from the authentication request
     */
    private class AuthenticationResponseListener
            implements Response.Listener<JSONObject>, Response.ErrorListener {
        /**
         * Process responses for POST requests that completed normally. The response
         * indicates whether login was successful or not.
         * @param authenticationResponse the response to the request
         */
        @Override
        public void onResponse(JSONObject authenticationResponse) {
            try {
                getApplicationInstance().setLoggedIn(authenticationResponse.getBoolean("loginSuccessful"));
                getApplicationInstance().setJwt(authenticationResponse.getString("jwt"));
            }
            catch (Exception e) {
                //There was a problem parsing the response from the server
                Log.e(TAG, e.getMessage());
                showToast(e.getMessage());
            }
            if (getApplicationInstance().isLoggedIn()) {
                startMyAccount();
            }
            else {
                showToast("Unable to log in");
                startWelcome();
            }
        }

        /**
         * Process responses from POST requests that failed
         * @param error the error information associated with the failure
         */
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Call to login failed: " + error.getMessage());
            showToast("Unable to log in: " + error.getMessage());
            startWelcome();
        }

    }

}

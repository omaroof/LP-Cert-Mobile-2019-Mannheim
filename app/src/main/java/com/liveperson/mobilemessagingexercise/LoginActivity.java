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
import com.liveperson.mobilemessagingexercise.model.ApplicationStorage;
import com.liveperson.mobilemessagingexercise.model.AuthenticationResponse;

import org.json.JSONObject;

/**
 * Activity associated with the application LoginFred screen
 */
public class LoginActivity extends MobileMessagingExerciseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    boolean loginSuccessful = false;
    String jwt;

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
        RequestQueue authenticationQueue = Volley.newRequestQueue(this);
        AuthenticationResponseListener authenticationResponseListener = new AuthenticationResponseListener();
        JSONObject credentials = new JSONObject();
        try {
            credentials.put("userId", userId);
            credentials.put("password", password);
            System.out.println(credentials);
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage());
            showToast(e.getMessage());
            return false;
        }
        JsonObjectRequest authenticationRequest = new JsonObjectRequest(
                Request.Method.POST,
                getBrandServerBaseUrl() + "/authenticate",
                credentials,
                authenticationResponseListener,
                authenticationResponseListener
        );

        authenticationQueue.add(authenticationRequest);


        return loginSuccessful;
    }

    /**********************************************
     * Inner Classes
     *********************************************/
    private class LoginOnClickListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText userIdControl = findViewById(R.id.userId);
            EditText passwordControl = findViewById(R.id.password);
            boolean loginSuccessful = logUserIn(userIdControl.getText().toString(), passwordControl.getText().toString());
        }
    }

    private class AuthenticationResponseListener
            implements Response.Listener<JSONObject>, Response.ErrorListener {
        @Override
        public void onResponse(JSONObject authenticationResponse) {
            try {

                loginSuccessful = authenticationResponse.getBoolean("loginSuccessful");
                jwt = authenticationResponse.getString("jwt");
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
                showToast(e.getMessage());
            }
            if (loginSuccessful) {
                startMyAccount();
            }
            else {
                showToast("Unable to log in");
                startWelcome();
            }
        }
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "Call to login failed: " + error.getMessage());
            showToast("Unable to log in: " + error.getMessage());
            startWelcome();
        }

    }

}

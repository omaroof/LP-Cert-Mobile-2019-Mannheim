package com.liveperson.mobilemessagingexercise.model;

public class AuthenticationResponse {
    private boolean loginSuccessful;
    private String jwt;

    public AuthenticationResponse() {

    }

    /************************
     * Bean Methods
     ***********************/
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}

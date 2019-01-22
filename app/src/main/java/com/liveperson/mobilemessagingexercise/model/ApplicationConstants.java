package com.liveperson.mobilemessagingexercise.model;

/**
 * Store constants for the entire app, including any services
 */
public class ApplicationConstants {
    private static final String BRAND_SERVER_URL = "https://gdr3xb81x4.execute-api.eu-west-1.amazonaws.com/prod";
    private static final String LIVE_PERSON_APP_ID = "com.liveperson.mobilemessagingexercise";

    //TODO - Replace with your app's LiveEngage account number
    private static final String LIVE_PERSON_ACCOUNT_NUMBER = "20553802";

    public static String getBrandServerUrl() {
        return BRAND_SERVER_URL;
    }

    public static String getLivePersonAppId() {
        return LIVE_PERSON_APP_ID;
    }

    public static String getLivePersonAccountNumber() {
        return LIVE_PERSON_ACCOUNT_NUMBER;
    }

}

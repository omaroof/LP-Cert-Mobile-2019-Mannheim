package com.liveperson.mobilemessagingexercise.model;


import com.liveperson.mobilemessagingexercise.BuildConfig;

/**
 * Store constants for the entire app, including any services
 */
public class ApplicationConstants {
    public static final String BRAND_SERVER_URL = "http://livepersondummyidpmobilecert-env-4.g2iy94pv45.eu-central-1.elasticbeanstalk.com";
    //"http://10.0.2.2:3000";
    public static final String LIVE_PERSON_APP_ID = BuildConfig.APPLICATION_ID;//"com.liveperson.mobilemessagingexercise";
    public static final String FCM_TOKEN_NAME = "fcmToken";

    //TODO - Replace with your app's LiveEngage account number
    public static final String LIVE_PERSON_ACCOUNT_NUMBER = "83559791";

    public static final int LP_PUSH_NOTIFICATION_ID = 42;
    public static final String LP_PUSH_NOTIFICATION_CHANNNEL_ID = "lp_push_notification_channel";
    public static final String LP_IS_FROM_PUSH = "lp_is_from_push";

}

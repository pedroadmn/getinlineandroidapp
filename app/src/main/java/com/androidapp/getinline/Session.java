package com.androidapp.getinline;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Session {

    /**
     * SharedPreference instance
     */
    private SharedPreferences prefs;

    /**
     * Editor for Shared preferences
     */
    private SharedPreferences.Editor editor;

    /**
     * Sharedpref file name
     */
    private static final String PREF_NAME = "GetinLine";

    /**
     * All Shared Preferences Keys
     */
    private static final String IS_LOGIN = "IsLoggedIn";

    /**
     * User id
     */
    public static final String KEY_USER_ID = "KEY_USER_ID";

    /**
     * User name
     */
    public static final String KEY_USER_NAME = "KEY_USER_NAME";

    /**
     * Email address
     */
    public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";

    /**
     * User Token FCM
     */
    public static final String KEY_USER_TOKEN_FCM = "KEY_USER_TOKEN_FCM";

    /**
     * User url photo
     */
    public static final String KEY_USER_URL_PHOTO = "KEY_USER_URL_PHOTO";

    // Constructor
    public Session(Context context) {
        Context ctx = context;
        int PRIVATE_MODE = 0;
        prefs = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String name, String email, String id, String tokenGCM, String urlPhoto) {
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_TOKEN_FCM, tokenGCM);
        editor.putString(KEY_USER_URL_PHOTO, urlPhoto);
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USER_NAME, prefs.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_EMAIL, prefs.getString(KEY_USER_EMAIL, null));
        user.put(KEY_USER_ID, prefs.getString(KEY_USER_ID, null));
        user.put(KEY_USER_TOKEN_FCM, prefs.getString(KEY_USER_TOKEN_FCM, null));
        user.put(KEY_USER_URL_PHOTO, prefs.getString(KEY_USER_URL_PHOTO, null));

        return user;
    }

}

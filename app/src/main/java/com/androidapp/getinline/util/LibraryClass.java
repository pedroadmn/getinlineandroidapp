package com.androidapp.getinline.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LibraryClass {

    /**
     * Static attribute PREF
     */
    private static String PREF = "com.androidapp.getinline.PREF";

    /**
     * Firebase Database Reference
     */
    private static DatabaseReference firebase;

    /**
     * Library Class contructor
     */
    private LibraryClass() {
    }

    /**
     * Method to get Firebase Database Reference
     *
     * @return Firebase Database Reference
     */
    public static DatabaseReference getFirebase() {
        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    /**
     * Method to save on shared preference the pair provider : token
     *
     * @param context Context
     * @param key     Key
     * @param value   Value
     */
    public static void saveSP(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * Method to get token based on provider
     *
     * @param context Context
     * @param key     Key
     * @return Token
     */
    public static String getSP(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key, "");
        return token;
    }

}

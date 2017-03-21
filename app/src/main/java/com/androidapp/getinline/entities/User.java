package com.androidapp.getinline.entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidapp.getinline.util.LibraryClass;
import com.androidapp.getinline.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {

    private static String PROVIDER = "com.androidapp.getinline.entities.User.PROVIDER";

    /**
     * User id
     */
    private String id;

    /**
     * User name
     */
    private String name;

    /**
     * User email
     */
    private String email;

    /**
     * User password
     */
    private String password;

    /**
     * User new password
     */
    private String newPassword;

    /**
     * User FCM token
     */
    private String tokenFCM;

    /**
     * User constructor
     */
    public User() {
    }

    /**
     * Method to get User id
     * @return User id
     */
    public String getId() {
        return id;
    }

    /**
     * Method to set User id
     * @param id New user id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method to verify if it is logged with social network
     * @param context Context
     * @return True if it is logged with social network, False otherwise
     */
    public boolean isSocialNetworkLogged(Context context) {
        String token = getProviderSP(context);
        return (token.contains(Util.FACEBOOK) || token.contains(Util.GOOGLE));
    }

    /**
     * Method to get User name
     * @return User name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set User name
     * @param name New user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to set user name in map
     * @param map Map
     */
    private void setNameInMap(Map<String, Object> map) {
        if (getName() != null) {
            map.put(Util.NAME_KEY, getName());
        }
    }

    /**
     *  Method to set user name in map
     * @param name New user name
     */
    public void setNameIfNull(String name) {
        if (this.name == null) {
            this.name = name;
        }
    }

    /**
     * Method to get user email
     * @return User email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to set User email
     * @param email New user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Method to set user email in map
     * @param map New user email in map
     */
    private void setEmailInMap(Map<String, Object> map) {
        if (getEmail() != null) {
            map.put(Util.EMAIL_KEY, getEmail());
        }
    }

    /**
     * Method to set User email if null
     * @param email New user email
     */
    public void setEmailIfNull(String email) {
        if (this.email == null) {
            this.email = email;
        }
    }

    /**
     * Method to get User password
     * @return User password
     */
    @Exclude
    public String getPassword() {
        return password;
    }

    /**
     * Method to set User password
     * @param password New user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method to get User new password
     * @return User new password
     */
    @Exclude
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Method to set User new password
     * @param newPassword New Password
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Method to save on shared preference the pair: provider : key
     * @param context Context
     * @param token Token
     */
    public void saveProviderSP(Context context, String token) {
        LibraryClass.saveSP(context, PROVIDER, token);
    }

    /**
     * Method to get token by provider key
     * @param context Context
     * @return Token
     */
    private String getProviderSP(Context context) {
        return (LibraryClass.getSP(context, PROVIDER));
    }

    /**
     * Method to save user into firebase database
     * @param completionListener DatabaseReference.CompletionListener
     */
    public void saveDB(DatabaseReference.CompletionListener... completionListener) {
        DatabaseReference firebase = LibraryClass.getFirebase().child(Util.USERS_KEY_DB).child(getId());

        if (completionListener.length == 0) {
            firebase.setValue(this);
        } else {
            firebase.setValue(this, completionListener[0]);
        }
    }

    /**
     * Method to update the user info into database
     * @param completionListener DatabaseReference.CompletionListener
     */
    public void updateDB(DatabaseReference.CompletionListener... completionListener) {

        DatabaseReference firebase = LibraryClass.getFirebase().child(Util.USERS_KEY_DB).child(getId());

        Map<String, Object> map = new HashMap<>();
        setNameInMap(map);
        setEmailInMap(map);

        if (map.isEmpty()) {
            return;
        }

        if (completionListener.length > 0) {
            firebase.updateChildren(map, completionListener[0]);
        } else {
            firebase.updateChildren(map);
        }
    }

    /**
     * Method to remove user in firebase database
     * @param completionListener DatabaseReference.CompletionListener
     */
    public void removeDB(DatabaseReference.CompletionListener completionListener) {
        DatabaseReference firebase = LibraryClass.getFirebase().child(Util.USERS_KEY_DB).child(getId());
        firebase.setValue(null, completionListener);
    }


    public void contextDataDB(Context context) {
        DatabaseReference firebase = LibraryClass.getFirebase().child(Util.USERS_KEY_DB).child(getId());
        firebase.addListenerForSingleValueEvent((ValueEventListener) context);
    }

    /**
     * Method to get User FCM token
     * @return User FCM token
     */
    public String getTokenFCM() {
        return tokenFCM;
    }

    /**
     * Method to set User FCM token
     * @param tokenFCM New FCM token
     */
    public void setTokenFCM(String tokenFCM) {
        this.tokenFCM = tokenFCM;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewToken(TokenEvent tokenEvent){
        setTokenFCM(tokenEvent.getToken());
        saveDB();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.tokenFCM);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.tokenFCM = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
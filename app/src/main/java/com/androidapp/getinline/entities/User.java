package com.androidapp.getinline.entities;

import android.content.Context;

import com.androidapp.getinline.util.LibraryClass;
import com.androidapp.getinline.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class User {

    public static String PROVIDER = "com.androidapp.getinline.entities.User.PROVIDER";

    private String id;
    private String name;
    private String email;
    private String password;
    private String newPassword;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSocialNetworkLogged(Context context) {
        String token = getProviderSP(context);
        return (token.contains(Util.FACEBOOK) || token.contains(Util.GOOGLE));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setNameInMap(Map<String, Object> map) {
        if (getName() != null) {
            map.put(Util.NAME_KEY, getName());
        }
    }

    public void setNameIfNull(String name) {
        if (this.name == null) {
            this.name = name;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private void setEmailInMap(Map<String, Object> map) {
        if (getEmail() != null) {
            map.put(Util.EMAIL_KEY, getEmail());
        }
    }

    public void setEmailIfNull(String email) {
        if (this.email == null) {
            this.email = email;
        }
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void saveProviderSP(Context context, String token) {
        LibraryClass.saveSP(context, PROVIDER, token);
    }

    public String getProviderSP(Context context) {
        return (LibraryClass.getSP(context, PROVIDER));
    }

    public void saveDB(DatabaseReference.CompletionListener... completionListener) {
        DatabaseReference firebase = LibraryClass.getFirebase().child(Util.USERS_KEY_DB).child(getId());

        if (completionListener.length == 0) {
            firebase.setValue(this);
        } else {
            firebase.setValue(this, completionListener[0]);
        }
    }

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

    public void removeDB(DatabaseReference.CompletionListener completionListener) {
        DatabaseReference firebase = LibraryClass.getFirebase().child(Util.USERS_KEY_DB).child(getId());
        firebase.setValue(null, completionListener);
    }

    public void contextDataDB(Context context) {
        DatabaseReference firebase = LibraryClass.getFirebase().child(Util.USERS_KEY_DB).child(getId());
        firebase.addListenerForSingleValueEvent((ValueEventListener) context);
    }
}
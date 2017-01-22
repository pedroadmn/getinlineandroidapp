package com.androidapp.getinline.listener;

import android.util.Log;

import com.androidapp.getinline.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by pedroadmn on 1/22/2017.
 */

public class CustomChildEventListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        User u = dataSnapshot.getValue(User.class);
        Log.i("LogUser", "ADDED");
        Log.i("LogUser", "Name:" + u.getName());
        Log.i("LogUser", "Email:" + u.getEmail());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        User u = dataSnapshot.getValue(User.class);
        Log.i("LogUser", "CHANGED");
        Log.i("LogUser", "Name:" + u.getName());
        Log.i("LogUser", "Email:" + u.getEmail());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        User u = dataSnapshot.getValue(User.class);
        Log.i("LogUser", "REMOVED");
        Log.i("LogUser", "Name:" + u.getName());
        Log.i("LogUser", "Email:" + u.getEmail());
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

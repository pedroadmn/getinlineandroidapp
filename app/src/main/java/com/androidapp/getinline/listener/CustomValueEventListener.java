package com.androidapp.getinline.listener;

import android.util.Log;

import com.androidapp.getinline.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pedroadmn on 1/22/2017.
 */

public class CustomValueEventListener implements ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for(DataSnapshot d : dataSnapshot.getChildren()){
            User u = d.getValue(User.class);
            Log.i("LogUser", "Name:" + u.getName());
            Log.i("LogUser", "Email:" + u.getEmail());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

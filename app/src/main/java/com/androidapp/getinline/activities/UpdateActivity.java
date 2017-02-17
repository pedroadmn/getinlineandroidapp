package com.androidapp.getinline.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.androidapp.getinline.R;
import com.androidapp.getinline.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pedroadmn on 1/22/2017.
 */

public class UpdateActivity extends AppCompatActivity implements ValueEventListener, DatabaseReference.CompletionListener {

    private Toolbar toolbar;
    private User user;
    private AutoCompleteTextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        toolbar.setTitle(getResources().getString(R.string.update_profile));
        name = (AutoCompleteTextView) findViewById(R.id.name);
        user = new User();
        user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.contextDataDB(this);
    }

    public void update(View view){
        user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.setName(name.getText().toString());
        user.updateDB(UpdateActivity.this);
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if( databaseError != null ){
            FirebaseCrash.report( databaseError.toException() );
            Toast.makeText( this, getResources().getString(R.string.failed) + databaseError.getMessage(), Toast.LENGTH_LONG ).show();
        }
        else{
            Toast.makeText( this, getResources().getString(R.string.successfully_updated), Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User u = dataSnapshot.getValue(User.class);
        name.setText(u.getName());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.report(databaseError.toException() );
    }
}

package com.androidapp.getinline.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidapp.getinline.R;
import com.androidapp.getinline.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RemoveUserActivity extends AppCompatActivity
        implements ValueEventListener, DatabaseReference.CompletionListener {

    /**
     * A standard toolbar for use within application content.
     */
    private Toolbar toolbar;

    /**
     * A User object
     */
    private User user;

    /**
     * The user password variable
     */
    private EditText password;

    /**
     * The entry point of the Firebase Authentication SDK.
     */
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    /**
     * Method to initiate the RemoveUserActivity screen layout. The action bar and user
     */
    private void init() {
        toolbar.setTitle(getResources().getString(R.string.remove_user));
        password = (EditText) findViewById(R.id.password);

        user = new User();
        user.setId(mAuth.getCurrentUser().getUid());
        user.contextDataDB(this);
    }

    /**
     * Method to get the user password and reauthenticate the user to then remove him
     *
     * @param view View
     */
    public void update(View view) {
        user.setPassword(password.getText().toString());
        reauthenticate();
    }

    /**
     * Method to reauthenticate user
     */
    private void reauthenticate() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(
                user.getEmail(),
                user.getPassword()
        );

        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            deleteUser();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report(e);
                Toast.makeText(
                        RemoveUserActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    /**
     * Method to delete the user
     */
    private void deleteUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            return;
        }

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (!task.isSuccessful()) {
                    return;
                }

                user.removeDB(RemoveUserActivity.this);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report(e);
                        Toast.makeText(
                                RemoveUserActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User u = dataSnapshot.getValue(User.class);
        user.setEmail(u.getEmail());
    }

    @Override
    public void onCancelled(DatabaseError firebaseError) {
        FirebaseCrash.report(firebaseError.toException());
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError != null) {
            FirebaseCrash.report(databaseError.toException());
        }

        Toast.makeText(
                RemoveUserActivity.this, getResources().getString(R.string.success_removed),
                Toast.LENGTH_SHORT
        ).show();
        finish();
    }
}
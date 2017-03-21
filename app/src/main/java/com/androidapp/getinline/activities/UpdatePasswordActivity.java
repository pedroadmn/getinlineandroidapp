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
import com.androidapp.getinline.util.Util;
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
import com.google.firebase.database.ValueEventListener;

public class UpdatePasswordActivity extends AppCompatActivity implements ValueEventListener {

    /**
     * A standard toolbar for use within application content.
     */
    private Toolbar toolbar;

    /**
     * A User object
     */
    private User user;

    /**
     * The password variable
     */
    private EditText password;

    /**
     * The new password variable
     */
    private EditText newPassword;

    /**
     * The entry point of the Firebase Authentication SDK.
     */
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

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
     * Method to initiate the UpdatePasswordActivity screen layout. The action bar and variable.
     */
    private void init() {
        toolbar.setTitle(getResources().getString(R.string.update_password));
        newPassword = (EditText) findViewById(R.id.new_password);
        password = (EditText) findViewById(R.id.password);

        user = new User();
        user.setId(mAuth.getCurrentUser().getUid());
        user.contextDataDB(this);
    }

    /**
     * Method to update password
     *
     * @param view View
     */
    public void update(View view) {
        user.setNewPassword(newPassword.getText().toString());
        user.setPassword(password.getText().toString());
        reauthenticate();
    }

    /**
     * Method to reauthenticate when the password is updated
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
                            updateData();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report(e);
                        Toast.makeText(
                                UpdatePasswordActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    /**
     * Method to update user password
     */
    private void updateData() {
        user.setNewPassword(newPassword.getText().toString());
        user.setPassword(password.getText().toString());

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            return;
        }

        firebaseUser
                .updatePassword(user.getNewPassword())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            newPassword.setText(Util.EMPTY);
                            password.setText(Util.EMPTY);

                            Toast.makeText(
                                    UpdatePasswordActivity.this, getResources().getString(R.string.password_updated),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report(e);
                        Toast.makeText(
                                UpdatePasswordActivity.this,
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
}
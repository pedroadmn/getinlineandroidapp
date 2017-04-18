package com.androidapp.getinline.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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



public class UpdateLoginFragment extends Fragment implements ValueEventListener {

    /**
     * A User object
     */
    private User user;

    /**
     * The new email variable
     */
    private AutoCompleteTextView newEmail;

    /**
     * The password variable
     */
    private EditText password;

    /**
     * The entry point of the Firebase Authentication SDK.
     */
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.activity_update_login, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update Login");
        Button button = (Button) rootView.findViewById(R.id.bt_update_login);
        newEmail = (AutoCompleteTextView) rootView.findViewById(R.id.email);
        password = (EditText) rootView.findViewById(R.id.password);

        user = new User();
        user.setId(mAuth.getCurrentUser().getUid());
        //user.contextDataDB(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setPassword(password.getText().toString());
                reauthenticate();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    /**
//     * Method to initiate the UpdateLoginActivity screen layout. The action bar and variables.
//     */
//    private void init() {
//        newEmail = (AutoCompleteTextView) findViewById(R.id.email);
//        password = (EditText) findViewById(R.id.password);
//
//        user = new User();
//        user.setId(mAuth.getCurrentUser().getUid());
//        user.contextDataDB(this);
//    }

//    /**
//     * Method to update email
//     *
//     * @param view View
//     */
//    public void update(View view) {
//        user.setPassword(password.getText().toString());
//        reauthenticate();
//    }

    /**
     * Method to reauthenticate when the email is updated
     */
    private void reauthenticate() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            return;
        }

        Log.d("USEREMAIL", user.getEmail());
        Log.d("USERPASSWORD", user.getPassword());

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
                                getContext(),
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    /**
     * Method to update user email
     */
    private void updateData() {
        user.setPassword(password.getText().toString());

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            return;
        }

        firebaseUser
                .updateEmail(newEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            user.setEmail(newEmail.getText().toString());
                            password.setText(Util.EMPTY);
                            user.updateDB();

                            Toast.makeText(
                                    getContext(), getResources().getString(R.string.email_updated),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                })
                .addOnFailureListener((Activity) getContext(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report(e);
                        Toast.makeText(
                                getContext(),
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User u = dataSnapshot.getValue(User.class);
        newEmail.setText(u.getEmail());
        user.setEmail(u.getEmail());
    }

    @Override
    public void onCancelled(DatabaseError firebaseError) {
        FirebaseCrash.report(firebaseError.toException());
    }



}

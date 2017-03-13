package com.androidapp.getinline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidapp.getinline.R;
import com.androidapp.getinline.entities.User;
import com.androidapp.getinline.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

public class SignUpActivity extends CommonActivity implements DatabaseReference.CompletionListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User user;
    private AutoCompleteTextView name;
    private TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser == null || user.getId() != null) {
                    return;
                }

                user.setId(firebaseUser.getUid());
                user.saveDB(SignUpActivity.this);
            }
        };
        initViews();
    }

    protected void initViews() {
        name = (AutoCompleteTextView) findViewById(R.id.name);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
        cancel = (TextView) findViewById(R.id.tv_cancel_signup);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCancel();
            }
        });
    }

    protected void initUser() {
        user = new User();
        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        String token = FirebaseInstanceId.getInstance().getToken();
        user.setTokenFCM(token);
    }

    public void sendSignUpData(View view) {
        initUser();
        saveUser();
    }

    private void saveUser() {
        if (!isFieldEmpty(user.getEmail(), user.getPassword())) {
            if (Util.validateEmail(getBaseContext(), user.getEmail())) {
                if (Util.validateEqualPassword(getBaseContext(), password.getText().toString(), confirmPassword.getText().toString())) {
                    openProgressBar();
                    mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        closeProgressBar();
                                    }
                                }
                            })
                            .addOnFailureListener(this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    FirebaseCrash.report(e);
                                    showSnackBar(e.getMessage());
                                }
                            });
                }
            }
        }
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        mAuth.signOut();
        showToast(getResources().getString(R.string.account_registered));
        closeProgressBar();
        finish();
    }

    public boolean isFieldEmpty(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showSnackBar(getResources().getString(R.string.empty_credentials));
            closeProgressBar();
            return true;
        }
        return false;
    }

    private void callCancel() {
        Intent goMain = new Intent(this, MainActivity.class);
        startActivity(goMain);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
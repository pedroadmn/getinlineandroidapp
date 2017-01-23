package com.androidapp.getinline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.androidapp.getinline.R;
import com.androidapp.getinline.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;


/**
 * Created by pedroadmn on 1/21/2017.
 */

public class LoginActivity extends CommonActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = getFirebaseAuthResultHandler();
        initViews();
        initUser();
    }

    protected void initViews(){
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    protected void initUser(){
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    public void callSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    /*public void callReset(View view){
        Intent intent = new Intent( this, ResetActivity.class );
        startActivity(intent);
    }*/

    public void sendLoginData(View view){
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginData()");
        openProgressBar();
        initUser();
        verifyLogin();
    }

    private void verifyLogged(){
        if(mAuth.getCurrentUser() != null){
            callMainActivity();
        } else {
            mAuth.addAuthStateListener( mAuthListener );
        }
    }

    private void verifyLogin(){
        FirebaseCrash.log("LoginActivity:verifyLogin()");
        user.saveProviderSP(LoginActivity.this, "");
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( !task.isSuccessful() ){
                            showSnackbar("Login falhou");
                            return;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report(e);
                    }
                });
    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler(){
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                if(userFirebase == null){
                    return;
                }
                if(user.getId() == null && isNameOk(user, userFirebase)){
                    user.setId(userFirebase.getUid());
                    user.setNameIfNull(userFirebase.getDisplayName());
                    user.setEmailIfNull(userFirebase.getEmail());
                    user.saveDB();
                }
                callMainActivity();
            }
        };
        return callback;
    }

    private boolean isNameOk(User user, FirebaseUser firebaseUser){
        return(user.getName() != null || firebaseUser.getDisplayName() != null);
    }

    private void callMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*private void accessLoginData(String provider, String... tokens){
        if(tokens != null && tokens.length > 0 && tokens[0] != null){
            user.saveProviderSP(LoginActivity.this, provider);
            mAuth.signInWithEmailAndPassword(credential);
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        verifyLogged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mAuthListener != null ){
            mAuth.removeAuthStateListener( mAuthListener );
        }
    }
}

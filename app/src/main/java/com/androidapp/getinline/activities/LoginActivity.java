package com.androidapp.getinline.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidapp.getinline.R;
import com.androidapp.getinline.entities.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.Arrays;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

/**
 * Created by pedroadmn on 1/21/2017.
 */

public class LoginActivity extends CommonActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN_GOOGLE = 7859;
    private static final int REQUEST_CODE = 1000 ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private TextView cancelButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessFacebookLoginData(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                FirebaseCrash.report( error );
                showSnackbar(error.getMessage());
            }
        });

        // GOOGLE SIGN IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("617151987057-es670jj5qfiuphb7u64gvrup3c8kdvum.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = getFirebaseAuthResultHandler();
        initViews();
        initUser();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == RC_SIGN_IN_GOOGLE ){

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();

            if( account == null ){
                showSnackbar(getResources().getString(R.string.google_login_failed));
                return;
            }

            accessGoogleLoginData(account.getIdToken() );
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

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

    private void accessFacebookLoginData(AccessToken accessToken){
        accessLoginData("facebook", (accessToken != null ? accessToken.getToken() : null));
    }

    private void accessGoogleLoginData(String accessToken){
        accessLoginData("google", accessToken);
    }

    private void accessLoginData( String provider, String... tokens ){
        if( tokens != null
                && tokens.length > 0
                && tokens[0] != null ){

            AuthCredential credential = FacebookAuthProvider.getCredential(tokens[0]);
            credential = provider.equalsIgnoreCase("google") ? GoogleAuthProvider.getCredential(tokens[0], null) : credential;

            user.saveProviderSP( LoginActivity.this, provider );
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){

                                showSnackbar(getResources().getString(R.string.social_login_failed));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseCrash.report( e );
                        }
                    });
        }
        else{
            mAuth.signOut();
        }
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

    protected void initViews(){
        cancelButton = (TextView) findViewById(R.id.cancel_button);
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

    public void callReset(View view){
        Intent intent = new Intent( this, ResetActivity.class );
        startActivity(intent);
    }

    public void callLoginForm(View v){
        findViewById(R.id.act_login_form).setVisibility(View.VISIBLE);
        findViewById(R.id.act_login_form).setEnabled(true);
        findViewById(R.id.email_login_form).setVisibility(View.INVISIBLE);
    }

    public void callCancel(){
        findViewById(R.id.act_login_form).setVisibility(View.INVISIBLE);
        findViewById(R.id.act_login_form).setEnabled(false);
        findViewById(R.id.email_login_form).setVisibility(View.VISIBLE);
    }

    public void loadTutorial(View view) {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(context.getString(R.string.empty_credentials), context.getString(R.string.empty_credentials),
                R.color.colorPrimaryDark, R.drawable.getinlinelogo,  R.drawable.getinlinelogo);

        TutorialItem tutorialItem2 = new TutorialItem(context.getString(R.string.empty_credentials), context.getString(R.string.empty_credentials),
                R.color.colorPrimaryDark, R.drawable.getinlinelogo,  R.drawable.getinlinelogo);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);

        return tutorialItems;
    }

    public void sendLoginData(View view){
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginData()");
        initUser();
        verifyLogin();
    }

    public void sendLoginFacebookData( View view ){
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginFacebookData()");
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "user_friends", "email"));
    }

    public void sendLoginGoogleData( View view ){
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginGoogleData()");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void callMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
        if(!isCredentialEmpty(user.getEmail(), user.getPassword())){
            openProgressBar();
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
        } else {
            showSnackbar(getResources().getString(R.string.empty_credentials));
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        FirebaseCrash.report(
                new Exception(connectionResult.getErrorCode()+": "+connectionResult.getErrorMessage()));
        showSnackbar( connectionResult.getErrorMessage() );
    }

    public boolean isCredentialEmpty(String email, String password){
        if(email.isEmpty() || password.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.act_login_form).isEnabled()) {
            findViewById(R.id.act_login_form).setEnabled(false);
            callCancel();
        } else {
            finish();
        }
    }
}

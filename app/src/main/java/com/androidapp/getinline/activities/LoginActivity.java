package com.androidapp.getinline.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidapp.getinline.R;
import com.androidapp.getinline.entities.User;
import com.androidapp.getinline.util.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class LoginActivity extends CommonActivity implements GoogleApiClient.OnConnectionFailedListener {

    /**
     * Google Request Code to onActivityResult
     */
    private static final int RC_SIGN_IN_GOOGLE = 7859;

    /**
     * Tutorial Request Code to onActivityResult
     */
    private static final int REQUEST_CODE_TUTORIAL = 1000;

    /**
     * The entry point of the Firebase Authentication SDK.
     */
    private FirebaseAuth mAuth;

    /**
     * Listener called when there is a change in the authentication state.
     */
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * The CallbackManager manages the callbacks into the FacebookSdk from an Activity's or
     * Fragment's onActivityResult() method.
     */
    private CallbackManager callbackManager;

    /**
     * The main entry point for Google Play services integration.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Cancel button to come back to main login page
     */
    private TextView cancel;

    /**
     * A User object
     */
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerFacebookCallBack();
        registerGoogleSignOption();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = getFirebaseAuthResultHandler();

        initViews();
        initUser();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCancel();
            }
        });
    }

    /**
     * Method to build the google sign in configurations options
     */
    private void registerGoogleSignOption() {
        // GOOGLE SIGN IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Util.GOOGLE_USER_CONTENT_KEY)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * Method to register facebook callback to login and permissions
     */
    private void registerFacebookCallBack() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessFacebookLoginData(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                FirebaseCrash.report(error);
                showSnackBar(error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();

            if (account == null) {
                showSnackBar(getResources().getString(R.string.google_login_failed));
                return;
            }

            accessGoogleLoginData(account.getIdToken());

        } else {
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
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Intermediary Method to send facebook token credential to accessLoginData method
     *
     * @param accessToken Token given by Facebook
     */
    private void accessFacebookLoginData(AccessToken accessToken) {
        accessLoginData(Util.FACEBOOK, (accessToken != null ? accessToken.getToken() : null));
    }

    /**
     * Intermediary Method to send google token credential to accessLoginData method
     *
     * @param accessToken Token given by Google
     */
    private void accessGoogleLoginData(String accessToken) {
        accessLoginData(Util.GOOGLE, accessToken);
    }

    /**
     * Method to sign in on Firebase Authentication with the provider(Facebook or Google)
     *
     * @param provider Login provider name(Facebook or Google)
     * @param tokens   Token given by provider(Facebook or Google)
     */

    private void accessLoginData(String provider, String... tokens) {
        if (tokens != null
                && tokens.length > 0
                && tokens[0] != null) {

            AuthCredential credential = FacebookAuthProvider.getCredential(tokens[0]);
            credential = provider.equalsIgnoreCase(Util.GOOGLE) ? GoogleAuthProvider.getCredential(tokens[0], null) : credential;

            user.saveProviderSP(LoginActivity.this, provider);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                showSnackBar(getResources().getString(R.string.social_login_failed));
                                closeProgressBar();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseCrash.report(e);
                            closeProgressBar();
                        }
                    });
        } else {
            mAuth.signOut();
        }
    }

    /**
     * Method to take some action when the auth state change
     *
     * @return FirebaseAuth.AuthStateListener
     */
    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler() {
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                if (userFirebase == null) {
                    return;
                }
                if (user.getId() == null && isNameOk(user, userFirebase)) {
                    user.setId(userFirebase.getUid());
                    user.setNameIfNull(userFirebase.getDisplayName());
                    user.setEmailIfNull(userFirebase.getEmail());
                    user.saveDB();
                }
                callMainActivity(userFirebase.getDisplayName(), userFirebase.getEmail(), userFirebase.getPhotoUrl());
            }
        };
        return callback;
    }

    /**
     * @param user         User
     * @param firebaseUser Firebase user
     * @return True if user name is ok, and False otherwise
     */
    private boolean isNameOk(User user, FirebaseUser firebaseUser) {
        return (user.getName() != null || firebaseUser.getDisplayName() != null);
    }

    /**
     * Method to initialize the views
     */
    protected void initViews() {
        cancel = (TextView) findViewById(R.id.cancel_button);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    /**
     * Method to initialize the user
     */
    protected void initUser() {
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setTokenFCM(FirebaseInstanceId.getInstance().getToken());
    }

    /**
     * Method to switch from LoginActivity to SignUpActivity
     *
     * @param view View
     */
    public void callSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Method to switch from LoginActivity to ResetActiviy
     *
     * @param view View
     */
    public void callReset(View view) {
        Intent intent = new Intent(this, ResetActivity.class);
        startActivity(intent);
    }

    /**
     * Method to make Login Form visible and main login screen invisible
     *
     * @param v View
     */
    public void callLoginForm(View v) {
        findViewById(R.id.act_login_form).setVisibility(View.VISIBLE);
        findViewById(R.id.act_login_form).setEnabled(true);
        findViewById(R.id.email_login_form).setVisibility(View.INVISIBLE);
        findViewById(R.id.tv_learn_more).setVisibility(View.INVISIBLE);
    }

    /**
     * Method to make Login Form invisible and main login screen visible
     */
    public void callCancel() {
        findViewById(R.id.act_login_form).setVisibility(View.INVISIBLE);
        findViewById(R.id.act_login_form).setEnabled(false);
        findViewById(R.id.email_login_form).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_learn_more).setVisibility(View.VISIBLE);
    }

    /**
     * Method to load the start tutorial
     *
     * @param view View
     */
    public void loadTutorial(View view) {
        Intent tutorialIntent = new Intent(this, MaterialTutorialActivity.class);
        tutorialIntent.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, Util.getTutorialItems(this));
        startActivityForResult(tutorialIntent, REQUEST_CODE_TUTORIAL);
    }

    /**
     * Method to send login data. To conventional login (email and password)
     *
     * @param view View
     */
    public void sendLoginData(View view) {
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginData()");
        initUser();
        verifyLogin();
    }

    /**
     * Method to send facebook login data.
     *
     * @param view View
     */
    public void sendLoginFacebookData(View view) {
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginFacebookData()");
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList(Util.PROFILE_PERMISSION, Util.FRIENDS_PERMISSION, Util.EMAIL_PERMISSION));
    }

    /**
     * Method to send google login data
     *
     * @param view View
     */
    public void sendLoginGoogleData(View view) {
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginGoogleData()");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    /**
     * Method to switch from LoginActivity to MainActivity
     *
     * @param profileName  User Name
     * @param profileEmail User Email
     * @param profilePhoto User Profile Photo
     */
    private void callMainActivity(String profileName, String profileEmail, Uri profilePhoto) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("profileEmail", profileEmail);
        intent.putExtra("profilePhoto", profilePhoto.toString());
        intent.putExtra("profileName", profileName);
        intent.putExtra(Util.KEY_USER, user);
        startActivity(intent);
        finish();
    }

    /**
     * Method to verify if the user is already logged.
     */
    private void verifyLogged() {
        if (mAuth.getCurrentUser() != null) {
            callMainActivity(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail(), mAuth.getCurrentUser().getPhotoUrl());
        } else {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    /**
     * Method to verify the conventional login(email and password).
     */
    private void verifyLogin() {
        FirebaseCrash.log("LoginActivity:verifyLogin()");
        if (!isCredentialEmpty(user.getEmail(), user.getPassword())) {
            if (Util.validateEmail(getBaseContext(), user.getEmail())) {
                openProgressBar();
                user.saveProviderSP(LoginActivity.this, Util.EMPTY);
                mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    showSnackBar(getResources().getString(R.string.failed));
                                    closeProgressBar();
                                    return;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report(e);
                        closeProgressBar();
                    }
                });
            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        FirebaseCrash.report(
                new Exception(connectionResult.getErrorCode() + ": " + connectionResult.getErrorMessage()));
        showSnackBar(connectionResult.getErrorMessage());
    }

    /**
     * Method to verify if the credentials are empty
     *
     * @param email    Email
     * @param password Password
     * @return True if credentials are empty, and False otherwise
     */
    public boolean isCredentialEmpty(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showSnackBar(getResources().getString(R.string.empty_credentials));
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.act_login_form).isEnabled()) {
            findViewById(R.id.act_login_form).setEnabled(false);
            callCancel();
        } else {
            finish();
        }
    }

}

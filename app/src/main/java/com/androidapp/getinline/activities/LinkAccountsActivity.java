package com.androidapp.getinline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;

public class LinkAccountsActivity extends CommonActivity
        implements GoogleApiClient.OnConnectionFailedListener, DatabaseReference.CompletionListener {

    /**
     * Google Request Code to onActivityResult
     */
    private static final int RC_SIGN_IN_GOOGLE = 7859;

    /**
     * The entry point of the Firebase Authentication SDK.
     */
    private FirebaseAuth mAuth;

    /**
     * A User object
     */
    private User user;

    /**
     * The CallbackManager manages the callbacks into the FacebookSdk from an Activity's or
     * Fragment's onActivityResult() method.
     */
    private CallbackManager callbackManager;

    /**
     * The main entry point for Google Play services integration.
     */
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_accounts);

        registerFacebookCallBack();
        registerGoogleSignOption();

        mAuth = FirebaseAuth.getInstance();
        initViews();
        initUser();
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

    /**
     * Intermediary Method to send conventional credential to method accessLoginData
     * @param email Email
     * @param password Password
     */
    private void accessEmailLoginData(String email, String password) {
        accessLoginData(Util.EMAIL_KEY, email, password);
    }

    /**
     * Intermediary Method to send facebook token credential to method accessLoginData
     * @param accessToken Token given by Facebook
     */
    private void accessFacebookLoginData(AccessToken accessToken) {
        accessLoginData(Util.FACEBOOK, (accessToken != null ? accessToken.getToken() : null)
        );
    }

    /**
     * Intermediary Method to send google token credential to accessLoginData method
     * @param accessToken Token given by Google
     */
    private void accessGoogleLoginData(String accessToken) {
        accessLoginData(Util.GOOGLE, accessToken);
    }

    /**
     * Method to access login credentials of some provider(Email, Facebook or Google) and link with existent account
     * @param provider Login provider name(Email, Facebook or Google)
     * @param tokens Token given by provider(Facebook or Google)
     */
    private void accessLoginData(final String provider, String... tokens) {
        if (tokens != null
                && tokens.length > 0
                && tokens[0] != null) {

            AuthCredential credential = FacebookAuthProvider.getCredential(tokens[0]);
            credential = provider.equalsIgnoreCase(Util.GOOGLE) ? GoogleAuthProvider.getCredential(tokens[0], null) : credential;
            credential = provider.equalsIgnoreCase(Util.EMAIL_KEY) ? EmailAuthProvider.getCredential(tokens[0], tokens[1]) : credential;

            mAuth
                    .getCurrentUser()
                    .linkWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            closeProgressBar();
                            if (!task.isSuccessful()) {
                                return;
                            }

                            initButtons();
                            showSnackBar("Provider account " + provider + " linked successfully.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseCrash.report(e);
                            closeProgressBar();
                            showSnackBar("Error: " + e.getMessage());
                        }
                    });
        } else {
            mAuth.signOut();
        }
    }

    /**
     * Method to initialize the views
     */
    protected void initViews() {
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        initButtons();
    }

    /**
     * Method to initiate the button to link or unlink accounts
     */
    private void initButtons() {
        setButtonLabel(
                R.id.email_sign_in_button,
                EmailAuthProvider.PROVIDER_ID,
                R.string.action_link,
                R.string.action_unlink,
                R.id.til_email,
                R.id.til_password
        );

        setButtonLabel(
                R.id.email_sign_in_facebook_button,
                FacebookAuthProvider.PROVIDER_ID,
                R.string.action_link_facebook,
                R.string.action_unlink_facebook
        );

        setButtonLabel(
                R.id.email_sign_in_google_button,
                GoogleAuthProvider.PROVIDER_ID,
                R.string.action_link_google,
                R.string.action_unlink_google
        );

    }

    /**
     * Method to set the button labels
     * @param buttonId Button Id
     * @param providerId Provider Id
     * @param linkId Link Id
     * @param unlinkId Unlink Id
     * @param fieldsIds Fields Id
     */
    private void setButtonLabel(
            int buttonId,
            String providerId,
            int linkId,
            int unlinkId,
            int... fieldsIds) {

        if (isALinkedProvider(providerId)) {
            ((Button) findViewById(buttonId)).setText(getString(unlinkId));
            showHideFields(false, fieldsIds);
        } else {
            ((Button) findViewById(buttonId)).setText(getString(linkId));
            showHideFields(true, fieldsIds);
        }
    }

    /**
     * Method to show or hide fields
     * @param status True if it is a linked provider, False otherwise
     * @param ids Fields Id
     */
    private void showHideFields(boolean status, int... ids) {
        for (int id : ids) {
            findViewById(id).setVisibility(status ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isALinkedProvider(String providerId) {

        for (UserInfo userInfo : mAuth.getCurrentUser().getProviderData()) {

            if (userInfo.getProviderId().equals(providerId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to initialize the user
     */
    protected void initUser() {
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    /**
     * Method to unlink account if it is a a linked provider, or link account otherwise
     * @param view View
     */
    public void sendLoginData(View view) {
        if (isALinkedProvider(EmailAuthProvider.PROVIDER_ID)) {
            unlinkProvider(EmailAuthProvider.PROVIDER_ID);
            return;
        }

        openProgressBar();
        initUser();
        accessEmailLoginData(user.getEmail(), user.getPassword());
    }

    /**
     * Method to unlink account if it is an linked provider, or link account otherwise
     * @param view View
     */
    public void sendLoginFacebookData(View view) {

        if (isALinkedProvider(FacebookAuthProvider.PROVIDER_ID)) {
            unlinkProvider(FacebookAuthProvider.PROVIDER_ID);
            return;
        }

        LoginManager
                .getInstance()
                .logInWithReadPermissions(
                        this,
                        Arrays.asList(Util.PROFILE_PERMISSION, Util.FRIENDS_PERMISSION, Util.EMAIL_PERMISSION)
                );
    }

    /**
     * Method to unlink account if it is an linked provider, or link account otherwise
     * @param view View
     */
    public void sendLoginGoogleData(View view) {
        if (isALinkedProvider(GoogleAuthProvider.PROVIDER_ID)) {
            unlinkProvider(GoogleAuthProvider.PROVIDER_ID);
            return;
        }

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        FirebaseCrash.report(new Exception(connectionResult.getErrorMessage()));
        showSnackBar(connectionResult.getErrorMessage());
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError != null) {
            FirebaseCrash.report(databaseError.toException());
        }

        mAuth.getCurrentUser().delete();
        mAuth.signOut();
        finish();
    }

    /**
     * Method to unlink account
     * @param providerId
     */
    private void unlinkProvider(final String providerId) {

        mAuth
                .getCurrentUser()
                .unlink(providerId)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        initButtons();
                        showSnackBar("Provider account " + providerId + " unlinked successfully.");

                        if (isLastProvider(providerId)) {
                            user.setId(mAuth.getCurrentUser().getUid());
                            user.removeDB(LinkAccountsActivity.this);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report(e);
                showSnackBar("Error: " + e.getMessage());
            }
        });
    }

    /**
     * Method to know if the account is the last provider link
     * @param providerId Provider Id
     * @return True if provider list is equal 0 or equal 1 and provider id is by Email
     */
    private boolean isLastProvider(String providerId) {
        int size = mAuth.getCurrentUser().getProviders().size();
        return (size == 0 || (size == 1 && providerId.equals(EmailAuthProvider.PROVIDER_ID)));
    }
}
package com.androidapp.getinline.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public abstract class CommonActivity extends AppCompatActivity {

    /**
     * User email variable
     */
    protected AutoCompleteTextView email;

    /**
     * User password and confirmPassword variable
     */
    protected EditText password, confirmPassword;

    /**
     * Visual indicator of progress in some operation
     */
    protected ProgressBar progressBar;

    /**
     * Method to provide lightweight feedback about an operation
     *
     * @param message Message
     */
    protected void showSnackBar(String message) {
        Snackbar.make(progressBar, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /**
     * Method to provides simple feedback about an operation in a small popup
     *
     * @param message Message
     */
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Method to make progress bar visible
     */
    protected void openProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Methdo to close progress bar
     */
    protected void closeProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Abstract method where activities will init the views
     */
    abstract protected void initViews();

    /**
     * Abstract method where activies will init an user
     */
    abstract protected void initUser();

}
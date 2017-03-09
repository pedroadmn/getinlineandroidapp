package com.androidapp.getinline.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidapp.getinline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;

import static android.R.id.message;
import static com.androidapp.getinline.R.string.reset;

public class ResetActivity extends CommonActivity {

    private Toolbar toolbar;
    private AutoCompleteTextView email;
    private FirebaseAuth firebaseAuth;
    private Button resetButton;
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    public void reset(){
        if (!isFieldEmpty(email.getText().toString())){
        firebaseAuth
            .sendPasswordResetEmail(email.getText().toString())
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        email.setText("");
                        Toast.makeText(ResetActivity.this, getResources().getString(R.string.access_recovery_initialized), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ResetActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseCrash.report( e );
                }
            });
        }else{
            showToast(getResources().getString(R.string.empty_email_reset));
        }
    }

    @Override
    protected void initViews() {
        toolbar.setTitle( getResources().getString(R.string.reset) );
        email = (AutoCompleteTextView) findViewById(R.id.email);
        resetButton = (Button) findViewById(R.id.bt_reset);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    public boolean isFieldEmpty(String email){
        return email.isEmpty();
    }

    @Override
    protected void initUser() {

    }
}
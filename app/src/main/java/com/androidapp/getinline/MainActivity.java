package com.androidapp.getinline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.androidapp.getinline.listener.CustomChildEventListener;
import com.androidapp.getinline.listener.CustomValueEventListener;
import com.androidapp.getinline.util.LibraryClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button btLogout;
    private CustomValueEventListener customValueEventListener;
    private CustomChildEventListener customChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = LibraryClass.getFirebase().child("users");
        //customValueEventListener = new CustomValueEventListener();
        //databaseReference.addListenerForSingleValueEvent(customValueEventListener);
        //customChildEventListener = new CustomChildEventListener();
        //databaseReference.addValueEventListener(customValueEventListener);
        //databaseReference.addChildEventListener(customChildEventListener);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };



        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(authStateListener);
        databaseReference = LibraryClass.getFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rv_users);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //databaseReference.removeEventListener(customValueEventListener);
        //databaseReference.removeEventListener(customChildEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            finish();
        } else if(id == R.id.action_update){
            startActivity(new Intent(this, UpdateActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

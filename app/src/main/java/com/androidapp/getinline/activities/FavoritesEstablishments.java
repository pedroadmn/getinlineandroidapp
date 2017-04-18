package com.androidapp.getinline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.androidapp.getinline.R;
import com.androidapp.getinline.Session;
import com.androidapp.getinline.adapters.EstablishmentAdapter;
import com.androidapp.getinline.entities.Establishment;
import com.androidapp.getinline.listener.ClickListener;
import com.androidapp.getinline.listener.RecyclerTouchListener;
import com.androidapp.getinline.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoritesEstablishments extends AppCompatActivity{

    /**
     * Recycle view to list of favorite establishments
     */
    RecyclerView mRecyclerView;

    /**
     * Establishment Adapter
     */
    private EstablishmentAdapter mEstablishmentAdapter;

    /**
     * Establishment List
     */
    public List<Establishment> establishments;

    /**
     * Search View
     */
    private SearchView mSearchView;

    /**
     * Progress Bar
     */
    private ProgressBar progressBar;

    /**
     * Map to retrieve user info from Shared Preference
     */
    private HashMap<String, String> userSession;

    /**
     * A standard toolbar for use within application content.
     */
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_establishments);

        Session session = new Session(this);
        userSession = session.getUserDetails();
        toolbar = (Toolbar) findViewById(R.id.fe_toolbar);
        toolbar.setTitle("Favorite Establishments");

        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateEstablishmentList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressBar = (ProgressBar) findViewById(R.id.pb_fe_fetching_data_establishments);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_favorites_establishments);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        updateEstablishmentList();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getBaseContext(), EstablishmentActivity.class);
                intent.putExtra(Util.KEY_ESTABLISHMENT, establishments.get(position));
                intent.putExtra(Util.KEY_USER, getIntent().getParcelableExtra(Util.KEY_USER));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    /**
     * Method to populate the establishment list
     */
    private void updateEstablishmentList() {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        final DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference("users");

        establishments = new ArrayList<>();

        mFirebaseDatabase.child(userSession.get(Session.KEY_USER_ID)).child("favoritesEstablishments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                establishments.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Establishment establishment = postSnapshot.getValue(Establishment.class);
                    establishments.add(establishment);
                }

                mEstablishmentAdapter = new EstablishmentAdapter(getBaseContext(), establishments);
                mRecyclerView.setAdapter(mEstablishmentAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}

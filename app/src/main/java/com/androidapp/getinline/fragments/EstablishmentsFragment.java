package com.androidapp.getinline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidapp.getinline.R;
import com.androidapp.getinline.activities.EstablishmentActivity;
import com.androidapp.getinline.adapters.EstablishmentAdapter;
import com.androidapp.getinline.entities.Establishment;
import com.androidapp.getinline.interfaces.EstablishmentsAPI;
import com.androidapp.getinline.listener.ClickListener;
import com.androidapp.getinline.listener.RecyclerTouchListener;
import com.androidapp.getinline.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.androidapp.getinline.R.string.search;
import static com.facebook.FacebookSdk.getApplicationContext;

public class EstablishmentsFragment extends Fragment implements SearchView.OnQueryTextListener {

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
     * Establishment Fragment constructor
     */
    public EstablishmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateEstablishmentList();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.list_establishments_fragment, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_fetching_data_establishments);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_establishments);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), EstablishmentActivity.class);
                intent.putExtra(Util.KEY_ESTABLISHMENT, establishments.get(position));
                intent.putExtra(Util.KEY_USER, getActivity().getIntent().getParcelableExtra(Util.KEY_USER));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return rootView;
    }

    /**
     * Method to populate the establishment list
     */
    private void updateEstablishmentList() {
        getRetrofitArray();
    }

    void getRetrofitArray() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EstablishmentsAPI api = retrofit.create(EstablishmentsAPI.class);

        Call<List<Establishment>> call = api.getEstablishmentDetails();

        call.enqueue(new Callback<List<Establishment>>() {
            @Override
            public void onResponse(Response<List<Establishment>> response, Retrofit retrofit) {
                try {
                    establishments = response.body();
                    mEstablishmentAdapter = new EstablishmentAdapter(getContext(), establishments);
                    mRecyclerView.setAdapter(mEstablishmentAdapter);
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mEstablishmentAdapter.filter(newText);
        return true;
    }

    /**
     * Method to set up the search view
     */
    private void setupSearchView() {
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getResources().getString(search));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        setupSearchView();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

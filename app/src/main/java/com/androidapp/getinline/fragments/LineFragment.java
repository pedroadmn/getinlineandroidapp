package com.androidapp.getinline.fragments;

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

import com.androidapp.getinline.R;
import com.androidapp.getinline.adapters.EstablishmentAdapter;
import com.androidapp.getinline.entities.Establishment;
import com.androidapp.getinline.interfaces.RetrofitArrayAPI;
import com.androidapp.getinline.util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.androidapp.getinline.R.string.search;
import static com.facebook.FacebookSdk.getApplicationContext;

public class LineFragment extends Fragment implements SearchView.OnQueryTextListener {

    /**
     * Recycle View
     */
    RecyclerView mRecyclerView;

    /**
     * Search View
     */
    private SearchView mSearchView;

    /**
     * Establishment Adapter
     */
    private EstablishmentAdapter mEstablishmentAdapter;

    /**
     * Establishment List
     */
    public List<Establishment> establishments;

    /**
     * Line Fragment constructor
     */
    public LineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.list_line_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_line);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
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

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        Call<List<Establishment>> call = service.getEstablishmentDetails();

        call.enqueue(new Callback<List<Establishment>>() {
            @Override
            public void onResponse(Response<List<Establishment>> response, Retrofit retrofit) {

                try {

                    List<Establishment> establishmentData = response.body();
                    Log.d("RETROFIT", establishmentData.get(0).toString());

                    for (int i = 0; i < establishmentData.size(); i++) {
                        Establishment est = new Establishment(establishmentData.get(i).getUrlPhoto(), establishmentData.get(i).getName(), establishmentData.get(i).getWebSite(), establishmentData.get(i).getEmail(),
                                establishmentData.get(i).get_id(), establishmentData.get(i).getSize(), establishmentData.get(i).getAttendingTime());

                        establishments.add(est);

                        mEstablishmentAdapter = new EstablishmentAdapter(getContext(), establishments);
                        mRecyclerView.setAdapter(mEstablishmentAdapter);
                    }

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
        establishments = new ArrayList<>();
        updateEstablishmentList();
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
}

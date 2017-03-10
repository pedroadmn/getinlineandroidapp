package com.androidapp.getinline.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.getinline.R;
import com.androidapp.getinline.adapters.EstablishmentAdapter;
import com.androidapp.getinline.entities.Establishment;

import java.util.ArrayList;
import java.util.List;

import static com.androidapp.getinline.R.string.search;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by pedroadmn-PC on 3/9/2017.
 */

public class LineFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private EstablishmentAdapter mEstablishmentAdapter;
    public List<Establishment> establishments = new ArrayList<>();
    private RecyclerView mRecyclerView;

    public LineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateEstablishmentList();
        mEstablishmentAdapter = new EstablishmentAdapter(getContext(), establishments);
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
        mRecyclerView.setAdapter(mEstablishmentAdapter);
        return rootView;
    }

    private void updateEstablishmentList() {

        Establishment e1 = new Establishment(getResources().getDrawable(R.mipmap.ic_launcher), "Estabelecimento", "www.estabelecimento.com", "estabelecimento1@gmail.com", "123456", "10", "20");
        Establishment e2 = new Establishment(getResources().getDrawable(R.mipmap.ic_launcher), "Cabeleireiro", "www.cabeleireito.com", "estabelecimento2@gmail.com", "123457", "15", "25");
        Establishment e3 = new Establishment(getResources().getDrawable(R.mipmap.ic_launcher), "Oficina", "www.oficina.com.br", "estabelecimento3@gmail.com", "123458", "5", "15");
        Establishment e4 = new Establishment(getResources().getDrawable(R.mipmap.ic_launcher), "Banco", "www.banco.com.br", "estabelecimento4@gmail.com", "123459", "6", "10");
        Establishment e5 = new Establishment(getResources().getDrawable(R.mipmap.ic_launcher), "Padaria", "www.padaria.com.br", "estabelecimento5@gmail.com", "123451", "10", "20");
        establishments.add(e1);
        establishments.add(e2);
        establishments.add(e3);
        establishments.add(e4);
        establishments.add(e5);
    }

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

package com.androidapp.getinline.fragments;

import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.getinline.R;
import com.androidapp.getinline.adapters.ViewPagerAdapter;


public class ViewPagerTabFragment extends Fragment {


    public static ViewPagerTabFragment newInstance(){
        return new ViewPagerTabFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("ONCREATEVIEWVPTFRAG", "ENTROU");

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.view_pager_tab_frag, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    /**
     * Method to build the view pager with the correspondent fragments
     *
     * @param viewPager View Pager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new EstablishmentsFragment(), getResources().getString(R.string.establishments));
        adapter.addFragment(new LineFragment(), getResources().getString(R.string.inline));
        viewPager.setAdapter(adapter);
    }
}

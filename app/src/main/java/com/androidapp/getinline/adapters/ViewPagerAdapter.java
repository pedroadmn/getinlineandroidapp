package com.androidapp.getinline.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    /**
     * Fragment List
     */
    private final List<Fragment> mFragmentList = new ArrayList<>();

    /**
     * Fragment´s Title List
     */
    private final List<String> mFragmentTitleList = new ArrayList<>();

    /**
     * View Pager Adapter constructor
     *
     * @param manager Fragment Manager
     */
    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * Method to add Fragment and Fragment title to Lists
     *
     * @param fragment Fragment
     * @param title    Fragment´s Title
     */
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}

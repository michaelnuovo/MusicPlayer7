package com.example.michael.myapplication.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by Michael on 1/12/2016.
 */
public class PageAdapterMainActivity extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String title;

    public PageAdapterMainActivity(FragmentManager fm, List<Fragment> fragments) {

        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return this.fragments.get(position);
    }

    @Override
    public int getCount() {

        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {title = "LISTS";}
        if (position == 1) {title = "LIKES";}
        if (position == 2) {title = "SONGS";}
        if (position == 3) {title = "ARTISTS";}
        if (position == 4) {title = "ALBUMS";}


        return title;
    }
}
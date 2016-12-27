package com.example.zzt.mediaplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zzt on 2016/12/18.
 */

public class MainAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    public MainAdapter(FragmentManager fm , ArrayList<Fragment> fragments) {
        super(fm);
        this.mFragments =fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}

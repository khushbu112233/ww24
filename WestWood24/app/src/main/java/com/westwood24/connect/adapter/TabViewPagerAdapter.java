package com.westwood24.connect.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.westwood24.connect.fragment.LatestFourthFragment;
import com.westwood24.connect.fragment.TopNewsFirstFragment;
import com.westwood24.connect.fragment.TrendingSecondFragment;
import com.westwood24.connect.fragment.VideoThirdFragment;

/**
 * Created by Dharmesh dudhat on 2/9/2016.
 */
//Extending FragmentStatePagerAdapter
public class TabViewPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public TabViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                TopNewsFirstFragment mTopNewsFirstFragment = new TopNewsFirstFragment();
                return mTopNewsFirstFragment;
            case 1:
                TrendingSecondFragment mTrendingSecondFragment = new TrendingSecondFragment();
                return mTrendingSecondFragment;
            case 2:
                VideoThirdFragment mVideoThirdFragment = new VideoThirdFragment();
                return mVideoThirdFragment;
            case 3:
                LatestFourthFragment mLatestFourthFragment = new LatestFourthFragment();
                return mLatestFourthFragment;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }


}
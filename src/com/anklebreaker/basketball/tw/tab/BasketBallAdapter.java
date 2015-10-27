package com.anklebreaker.basketball.tw.tab;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import com.anklebreaker.basketball.tw.R;
import com.viewpagerindicator.IconPagerAdapter;

/**
 * FragmentPagerAdapter
 * */
public class BasketBallAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    
    private static final String TAG = "BasketBallAdapter";
    private Activity mActivity;
    public static final String[] CONTENT = new String[] {"比賽球隊A", "比賽球隊B"};
    public final int[] ICONS = new int[] {
        R.drawable.perm_group_calendar,
        R.drawable.perm_group_camera,
        R.drawable.perm_group_device_alarms,
        R.drawable.perm_group_location,
    };
    
    public BasketBallAdapter(FragmentManager fm, Activity a) {
        super(fm);
        mActivity = a;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "getItem");
        return BasketFragment.newInstance(CONTENT[position % CONTENT.length], 
                mActivity);
    }
    @Override
    //取得每個tab上的字
    public CharSequence getPageTitle(int position) {
        Log.i(TAG, "getPageTitle");
        return CONTENT[position % CONTENT.length].toUpperCase();
    }
    //取得每個tab上的icon
    @Override
    public int getIconResId(int index) {
        Log.i(TAG, "getIconResId");
        return ICONS[index];
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount");
        return CONTENT.length;
    }
}
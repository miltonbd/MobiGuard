package com.applock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyLogger;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;

import de.greenrobot.event.EventBus;


public class ListAppLockContainerFragment extends BaseFragment {

    public ListAppLockFragment appLockFragment;
    public AppLockSettingFragment appLockSettingFragment;
    public  TabLayout tabLayout;
    public  ViewPager viewPager;
    public  int int_items = 1;

    public final static String blockedListTitle = "Block List";
    public final static String blockModelTitle = "Block Mode";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x =  inflater.inflate(R.layout.tab_layout,null);
        MyLogger.debug("inside " + getClass().getName());
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        FragmentManager manager=getActivity().getSupportFragmentManager();
        MyAdapter adapter=new MyAdapter(manager);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabsFromPagerAdapter(adapter);
        return x;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
            appLockFragment = new ListAppLockFragment();
            appLockSettingFragment = new AppLockSettingFragment();
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return appLockFragment;
                case 1 : return appLockSettingFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return blockedListTitle;
                case 1 :
                    return blockModelTitle;
            }
            return null;
        }
    }
    @Override
    public void backPressedInFragment() {
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(true));
    }
}

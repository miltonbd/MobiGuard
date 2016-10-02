package com.passwords;

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
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.passwords.category.PasswordCategoryFragment;
import com.passwords.password.PasswordsFragment;

import de.greenrobot.event.EventBus;

public class PasswordContainerFragment extends BaseFragment {

    public final PasswordsFragment passwordsFragment = new PasswordsFragment();
    public final PasswordCategoryFragment passwordCategoryFragment = new PasswordCategoryFragment();
    public  TabLayout tabLayout;
    public  ViewPager viewPager;
    public  int int_items = 2 ;

    public final static String passwordsTabTitle = "Passwords";
    public final static String categoryTabTitle = "Categories";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }


    public PasswordCategoryFragment getPasswordCategoryFragment() {
        return passwordCategoryFragment;
    }

    public PasswordsFragment getPasswordsFragment() {
        return passwordsFragment;
    }


    class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return passwordsFragment;
                case 1 : return passwordCategoryFragment;
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
                    return passwordsTabTitle;
                case 1 :
                    return categoryTabTitle;
            }
            return null;
        }
    }
    @Override
    public void backPressedInFragment() {
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(true));
    }
}

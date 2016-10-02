package com.callsms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.callsms.calls.BlockedNumberFragment;
import com.callsms.calls.BlockedCallsFragment;
import com.callsms.sms.BlockedSMSFragment;

/**
 * Created by milton on 7/03/16.
 */


public class CallSMSContainerFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public CallSMSContainerFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BlockedNumberFragment tab1 = new BlockedNumberFragment();
                return tab1;
            case 1:
                BlockedCallsFragment tab2 = new BlockedCallsFragment();
                return tab2;
            case 2:
                BlockedSMSFragment tab3 = new BlockedSMSFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
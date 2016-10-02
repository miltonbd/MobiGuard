package com.spy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spy.audiorecorder.AudioRecordFragment;
import com.spy.callrecorder.CallRecordFragment;
import com.spy.videorecorder.VideoRecorderFragment;

/**
 * Created by milton on 7/03/16.
 */


public class SpyContainerFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SpyContainerFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CallRecordFragment tab1 = new CallRecordFragment();
                return tab1;
            case 1:
                VideoRecorderFragment tab2 = new VideoRecorderFragment();
                return tab2;
            case 2:
                AudioRecordFragment tab3 = new AudioRecordFragment();
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
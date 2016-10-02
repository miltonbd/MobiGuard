package com.spy.videorecorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fs.lib.util.BaseFragment;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by milton on 15/02/16.
 */
public class VideoRecorderFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spy_video_record_list, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //EventBus.getDefault().register(this);
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
    }

    @Override
    public void backPressedInFragment() {

    }
}

package com.mobiguard;

import android.os.Bundle;

import com.mobiguard.R;
import com.github.machinarius.preferencefragment.PreferenceFragment;

import de.greenrobot.event.EventBus;

public  class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.setting);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnActionBarTitleChange("Setting"));
    }
}
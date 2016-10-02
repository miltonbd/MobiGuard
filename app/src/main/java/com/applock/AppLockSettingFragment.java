package com.applock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyLogger;
import com.mobiguard.MyApp;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.passwords.events.OnRefreshPasswordCategory;
import com.util.MyDatabaseHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class AppLockSettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    private MyDatabaseHelper db;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean systemAppInclude = false;
    AppPackageInfo apps;

    @Bind(R.id.checkBoxAppLockOn)
    CheckBox checkBoxAppLockOn;



    public AppLockSettingFragment() {
        //  EventBus.getDefault().register(this);
    }

    public void onEvent(OnRefreshPasswordCategory event) {
        loadApps();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applock_setting, null);
        ButterKnife.bind(this, view);
        checkBoxAppLockOn.setOnCheckedChangeListener(this);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        apps = new AppPackageInfo(getContext());
        loadApps();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnActionBarTitleChange("App Lock"));
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
    }

    private void loadApps() {
        ArrayList<AppInfoPojo> appsPackages = apps.getPackages(systemAppInclude);
        MyLogger.debug("Packages count " + appsPackages.size());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkBoxAppLockOn:
                if (isChecked) {

                } else {

                }
                break;
        }

    }
    @Override
    public void backPressedInFragment() {
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(true));
    }
}

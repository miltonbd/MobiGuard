package com.applock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.mobiguard.MyApp;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.passwords.events.OnBackPressed;
import com.passwords.events.OnRefreshPasswordCategory;
import com.util.MyDatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AddAppLockFragment extends BaseFragment implements MyEventListener {
    private MyDatabaseHelper db;
    private AddLockedAppAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean systemAppInclude = true;
    private AppPackageInfo apps;
    ArrayList<AppInfoPojo> items=new ArrayList<>();

    @Bind(R.id.actionbar_selected_items_count)
    TextView actionbar_selected_items_count;

    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.menu_action_save)
    ImageView menu_action_save;

    @Bind(R.id.menu_action_clear)
    ImageView menu_action_clear;

    @Bind(R.id.checkBoxIncludeSystemApp)
    CheckBox checkBoxIncludeSystemApp;

    public AddAppLockFragment() {
        //  EventBus.getDefault().register(this);
    }

    public void onEvent(OnRefreshPasswordCategory event) {
        loadApps();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applock_add, null);
        ButterKnife.bind(this, view);
        apps = new AppPackageInfo(getContext());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        loadApps();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnActionBarTitleChange("Add App to Locker."));
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
    }

    @OnClick(R.id.menu_action_save)
    public void menuActionSave(View view) {
        MyLogger.debug("Saving Add Image to Vault");
        saveLockedApps();
    }

    private void saveLockedApps() {
        for(Map.Entry<Integer, Boolean> entry:mAdapter.selectedItems.entrySet()) {
            AppInfoPojo item=mAdapter.items.get(entry.getKey());
            LockedApps lockedApps = new LockedApps();
            lockedApps.setAppName(item.getAppName());
            lockedApps.setPackageName(item.getPackageName());
            lockedApps.setIcon(item.getIcon());
            lockedApps.setVersionName(item.getVersionName());
            lockedApps.setVersionCode(item.getVersionCode());
            try {
                db.lockApp(lockedApps);
                EventBus.getDefault().post(new OnRefreshLockedApp());
                EventBus.getDefault().post(new OnBackPressed());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @OnCheckedChanged(R.id.checkBoxIncludeSystemApp)
    public void menuActionClear(CompoundButton btn, boolean isChecked) {
        systemAppInclude=isChecked;
        loadApps();
    }

    @OnClick(R.id.menu_action_clear)
    public void menuActionClear(View view) {
        MyLogger.debug("Clearning Add Image to Vault");
        mAdapter.clearSelections();
        actionbar_selected_items_count.setText(String.valueOf(0));
    }

    private void loadApps() {
        items = apps.getPackages(systemAppInclude);
        mAdapter = new AddLockedAppAdapter(items,R.layout.list_item_applock_add_list);
        mAdapter.setMyEventListener(this);
        MyLogger.debug("Apps loaded "+items.size());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void backPressedInFragment() {
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(true));
    }

    @Override
    public void onItemClick(int position, View v) {
        MyLogger.debug("onClick in AddLockedVideoFragment");
        AppInfoPojo item=mAdapter.items.get(position);
        mAdapter.toggleSelection(position);
        actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
    }

    @Override
    public void onItemLongClick(int position, View v) {

    }

    @Override
    public void onItemCheckedChange(int position, CompoundButton button, boolean checked) {

    }
}

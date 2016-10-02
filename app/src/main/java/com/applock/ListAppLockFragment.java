package com.applock;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.OnConfirmDialog;
import com.mobiguard.MyApp;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnAttachFragment;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.passwords.events.OnRefreshPasswordCategory;
import com.util.MyDatabaseHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class ListAppLockFragment extends BaseFragment implements MyEventListener {
    private MyDatabaseHelper db;
    private ListLockedAppAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.actionbar_selected_items_count)
    TextView actionbar_selected_items_count;

    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.textViewEmpty)
    TextView textViewEmpty;

    @Bind(R.id.textViewSelectedTitle)
    TextView textViewSelectedTitle;

    @Bind(R.id.menu_action_save)
    ImageView menu_action_save;

    @Bind(R.id.menu_action_clear)
    ImageView menu_action_clear;

    @Bind(R.id.bottomMenu)
    Toolbar bottomMenu;

    private List<LockedApps> items;
    private boolean isCabActive=false;
    private LockedApps lockedApps;

    public ListAppLockFragment() {
        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);
    }

    public void onEvent(OnRefreshPasswordCategory event) {
        loadApps();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applock_list, null);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        loadApps();
        MyLogger.debug("Apps loaded.");
        return view;
    }


    public void onEvent(OnRefreshLockedApp event){
        loadApps();
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().post(new OnActionBarTitleChange("App Locker"));
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
        makeBottomMenuAdd();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void makeBottomMenuAdd(){
        actionbar_selected_items_count.setVisibility(View.GONE);
        menu_action_clear.setVisibility(View.GONE);
        textViewSelectedTitle.setVisibility(View.GONE);
        menu_action_save.setImageResource(R.drawable.ic_add_white_48dp);
        textViewSelectedTitle.setVisibility(View.GONE);
    }

    private void makeBottomMenuMultipleSelect(){
        actionbar_selected_items_count.setVisibility(View.VISIBLE);
        menu_action_clear.setVisibility(View.VISIBLE);
        textViewSelectedTitle.setVisibility(View.VISIBLE);
        menu_action_save.setImageResource(R.drawable.ic_cab_done_holo_dark);
        textViewSelectedTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ////inflater.inflate(R.menu.new_app_to_locker, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                MyLogger.debug("Showing Add AddAppLockFragment");
                // Show Locked App Chooser.
                AddAppLockFragment fragment = new AddAppLockFragment();
                EventBus.getDefault().post(new OnAttachFragment(fragment));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.menu_action_save)
    public void onMenuActionClicked(View view) {

        if (!isCabActive) {
            AddAppLockFragment fragment = new AddAppLockFragment();
            EventBus.getDefault().post(new OnAttachFragment(fragment));
        } else {
            MyLogger.debug("in menu_action_save");
            OnConfirmDialog dialog=   new OnConfirmDialog(getContext(),"Do you want to Unlock?"){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            MyLogger.debug("Total apps to unlock "+mAdapter.selectedItems.size());
                            for(Map.Entry<Integer, Boolean> entry:mAdapter.selectedItems.entrySet()){
                                try {
                                    LockedApps lockedApp=mAdapter.items.get(entry.getKey());
                                    MyLogger.debug("Deleting App "+lockedApp);
                                    db.unlockApp(lockedApp);

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            mAdapter.clearSelections();
                            bottomMenu.setVisibility(View.GONE);
                            EventBus.getDefault().post(new OnActionBarTitleChange("App Locker"));
                            isCabActive=false;
                            loadApps();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.cancel();
                            break;
                    }
                }
            };
            dialog.show();
        }


    }


    private void loadApps() {
        try {
            items = this.db.getAllLockedApps();
            if (items.size() > 0) {
                textViewEmpty.setText(String.valueOf("Locked Apps"));
            } else {
                textViewEmpty.setText(String.valueOf("No Locked App Found."));
            }
            mAdapter = new ListLockedAppAdapter(items,R.layout.list_item_applock_list);
            mAdapter.setMyEventListener(this);
            mRecyclerView.setAdapter(mAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void backPressedInFragment() {
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(true));
    }
    @OnClick(R.id.menu_action_clear)
    public void menuActionClear(View view) {
        MyLogger.debug("Clearning Add Image to Vault");
        mAdapter.clearSelections();
        actionbar_selected_items_count.setText(String.valueOf(0));
        EventBus.getDefault().post(new OnActionBarTitleChange("App Locker"));
        isCabActive = false;
        makeBottomMenuAdd();
    }
    @Override
    public void onItemClick(int position, View v) {
        final LockedApps item = mAdapter.items.get(position);
        if (!isCabActive) { //
            MyLogger.debug("onItemClick in  " + getClass().getName());
            lockedApps = mAdapter.items.get(position);
            OnConfirmDialog dialog=new OnConfirmDialog(getContext(),"Do you want to unlock?") {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            try {
                                db.unlockApp(item);
                                loadApps();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.cancel();
                            break;

                    }
                }
            };
            dialog.show();
        } else {

            mAdapter.toggleSelection(position);
            actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
            if (mAdapter.selectedItems.size() == 0) {
                EventBus.getDefault().post(new OnActionBarTitleChange("App Locker"));
                isCabActive = false;
                makeBottomMenuAdd();

            }
        }
    }

    @Override
    public void onItemLongClick(int position, View v) {
        isCabActive = true;
        LockedApps item = mAdapter.items.get(position);
        mAdapter.toggleSelection(position);
        actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
        bottomMenu.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new OnActionBarTitleChange("Unlock App"));
        makeBottomMenuMultipleSelect();
    }

    @Override
    public void onItemCheckedChange(int position, CompoundButton button, boolean checked) {

    }
}

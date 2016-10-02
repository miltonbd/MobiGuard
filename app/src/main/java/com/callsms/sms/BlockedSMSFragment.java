package com.callsms.sms;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fs.lib.util.BaseAlertDialog;
import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.OnConfirmDialog;
import com.mobiguard.MyApp;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.R;
import com.passwords.password.OnRefreshPasswords;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by milton on 15/02/16.
 */
public  class BlockedSMSFragment extends BaseFragment implements MyEventListener {
    @Bind(R.id.actionbar_selected_items_count)
    TextView actionbar_selected_items_count;

    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.textViewEmpty)
    TextView textViewEmpty;


    @Bind(R.id.menu_action_save)
    ImageView menu_action_save;

    @Bind(R.id.menu_action_clear)
    ImageView menu_action_clear;

    @Bind(R.id.bottomMenu)
    Toolbar bottomMenu;

    private LinearLayoutManager mLayoutManager;
    private MyDatabaseHelper db;
    private List<BlockedSMS> items;
    private BaseAlertDialog onClickItemAlertDialog;
    private BlockedSMS blockedSMS;
    private boolean isCabActive=false;
    private BlockedSMSsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_locked_sms_list, null);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        initOnNumberClicked();
        loadItems();
        bottomMenu.setVisibility(View.GONE);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void  loadItems() {
        EventBus.getDefault().post(new OnRefreshPasswords());
        try {
            Dao<BlockedSMS, Long> passwordCategories= db.getBlockedSMSDao();
            QueryBuilder<BlockedSMS, Long> cb= passwordCategories.queryBuilder();
            cb.orderBy(MyDatabaseHelper.order, true);
            items = passwordCategories.query(cb.prepare());

            if (items.size()>0) {
                textViewEmpty.setVisibility(View.GONE);
            } else {
                textViewEmpty.setVisibility(View.VISIBLE);
                textViewEmpty.setText("No Blocked SMS Found.");
            }

            MyLogger.debug("Blocked SMS count " + items.size());
            mAdapter = new BlockedSMSsAdapter(items, R.layout.list_item_blocked_sms);
            mAdapter.setMyEventListener(this);
            mRecyclerView.setAdapter(mAdapter);

        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }
    }
    private void SMSNumber() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", blockedSMS.getBlockedNumber());
        smsIntent.putExtra("sms_body","");
        startActivity(smsIntent);
    }

    private void callNumber() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + blockedSMS.getBlockedNumber()));
        startActivity(intent);
    }
    private void deleteNumber(BlockedSMS blockedSMS) {
        MyLogger.debug("Deleting BlockedSMS "+blockedSMS);
        try {
            DeleteBuilder<BlockedSMS, Long> dao=  db.getBlockedSMSDao().deleteBuilder();
            dao.where().eq(MyDatabaseHelper.id, blockedSMS.getId());
            dao.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initOnNumberClicked() {
        MyLogger.debug("initOnNumberClicked in "+getClass().getName());
        CharSequence items[] = new CharSequence[]{"Call","SMS",
                "Delete",};

        onClickItemAlertDialog = new BaseAlertDialog(getContext(), R.string.dialog_title_blockList_sms_item, items) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // CallBaseMultipleSelectAdapter
                        callNumber();
                        break;
                    case 1: // SMS
                        SMSNumber();
                        break;
                    case 2:  // Delete
                        deleteNumber(blockedSMS);
                        loadItems();
                        break;
                }
            }
        };

    }
    @Override
    public void backPressedInFragment() {

    }

    @Override
    public void onItemClick(int position, View v) {
        if(!isCabActive){
            MyLogger.debug("onItemClick in  "+getClass().getName());
            blockedSMS = mAdapter.items.get(position);
            onClickItemAlertDialog.showDialog();
            // show Alert Dialog
        }else {
            BlockedSMS item=mAdapter.items.get(position);
            mAdapter.toggleSelection(position);
            actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
            if (mAdapter.selectedItems.size()==0){
                bottomMenu.setVisibility(View.GONE);
                EventBus.getDefault().post(new OnActionBarTitleChange("Blocked SMS"));
                isCabActive=false;
            }
        }
    }

    @OnClick(R.id.menu_action_save)
    public void menuActionSave(View view) {
        MyLogger.debug("Saving Add Image to Vault");
        OnConfirmDialog dialog=   new OnConfirmDialog(getContext(),"Do you want to delete?"){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        for ( Map.Entry<Integer, Boolean> item :mAdapter.selectedItems.entrySet()) {
                            BlockedSMS m= mAdapter.items.get(item.getKey());
                            deleteNumber(m);
                        }
                        mAdapter.clearSelections();
                        bottomMenu.setVisibility(View.GONE);
                        EventBus.getDefault().post(new OnActionBarTitleChange("Blocked SMS"));
                        isCabActive=false;
                        loadItems();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };
        dialog.show();
    }

    @OnClick(R.id.menu_action_clear)
    public void menuActionClear(View view) {
        MyLogger.debug("Clearning Add Image to Vault");
        mAdapter.clearSelections();
        actionbar_selected_items_count.setText(String.valueOf(0));
    }

    @Override
    public void onItemLongClick(int position, View v) {
        isCabActive=true;
        BlockedSMS item=mAdapter.items.get(position);
        mAdapter.toggleSelection(position);
        actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
        bottomMenu.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new OnActionBarTitleChange("Delete Blocked SMS"));
    }

    @Override
    public void onItemCheckedChange(int position,CompoundButton button, boolean checked) {

    }
    public void initOnClickNumber() {
        CharSequence items[] = new CharSequence[]{"Call","SMS","Delete"};

        onClickItemAlertDialog = new BaseAlertDialog(getContext(), R.string.dialog_title_blockList_call_item, items) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Call
                        callNumber();
                        break;
                    case 1: // SMS
                        SMSNumber();
                        break;
                    case 2:  // Delete
                        deleteNumber(blockedSMS);
                        loadItems();
                        break;
                }
            }
        };
    }
}

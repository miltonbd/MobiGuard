package com.callsms.calls;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.callsms.logs.SMSLogListDialogFragment;
import com.fs.lib.util.BaseAlertDialog;
import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.OnConfirmDialog;
import com.mobiguard.MyApp;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class BlockedNumberFragment extends BaseFragment implements MyEventListener {
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

    private List<BlockedCallSMSNumber> items = new ArrayList<>();
    private MyDatabaseHelper db;
    private BlockedNumberAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentManager fragmentManager;
    private BaseAlertDialog onNewNumberAlertDialog;
    private BaseAlertDialog onItemClickedAlertDialog;
    private BlockedCallSMSNumber blockedCallSMSNumber;
    private boolean isCabActive;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number_block_list, null);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        loadItems();
        initOnNewNumberChooseOptions();
        initOnClickAlertDialog();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
        makeBottomMenuAdd();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
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

    public void initOnClickAlertDialog() {
        CharSequence items[] = new CharSequence[]{"Call","SMS","Delete"};

        onItemClickedAlertDialog = new BaseAlertDialog(getContext(), R.string.dialog_title_blockList_call_item, items) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Call
                        callNumber();
                        break;
                    case 1: // SMS
                        smsNumber();
                        break;
                    case 2:  // Delete
                        editNumber();
                        break;
                    case 3:  // Delete
                        deleteNumber(blockedCallSMSNumber);
                        loadItems();
                        break;
                }
            }
        };
    }
    @OnClick(R.id.menu_action_save)
    public void OnMenuAddActionClicked(View view) {
        if (!isCabActive) {
            onNewNumberAlertDialog.showDialog();
        } else {
            MyLogger.debug("Saving Add Image to Vault");
            OnConfirmDialog dialog=   new OnConfirmDialog(getContext(),"Do you want to delete?"){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            for ( Map.Entry<Integer, Boolean> item :mAdapter.selectedItems.entrySet()) {
                                BlockedCallSMSNumber m= mAdapter.items.get(item.getKey());
                                deleteNumber(m);
                            }
                            mAdapter.clearSelections();
                            bottomMenu.setVisibility(View.GONE);
                            EventBus.getDefault().post(new OnActionBarTitleChange("Blocked Calls"));
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
    }

    public void onEvent(OnRefreshBlockedNumberList event) {
        loadItems();
    }


    private void smsNumber() {

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", blockedCallSMSNumber.getBlockedNumber());
        smsIntent.putExtra("sms_body", "");
        startActivity(smsIntent);

    }

    private void deleteNumber(BlockedCallSMSNumber blockedCallSMSNumber) {
        try {
            DeleteBuilder<BlockedCallSMSNumber, Long> dao = db.getBlackListedCallSMSNumberDao().deleteBuilder();
            dao.where().eq(MyDatabaseHelper.id, blockedCallSMSNumber.getId());
            dao.delete();
            loadItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editNumber() {
        CustomPhoneNumberDialog dialog = CustomPhoneNumberDialog.getInstance(blockedCallSMSNumber);
        dialog.show(getActivity().getSupportFragmentManager(), "Edit Number");
        onItemClickedAlertDialog.setCancelable(true);
    }

    private void callNumber() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + blockedCallSMSNumber.getBlockedNumber()));
        startActivity(intent);
    }

    private void initOnNewNumberChooseOptions() {
        CharSequence items[] = new CharSequence[]{"From Contact",
                "From Call Logs", "From Message", "From Custom"};

        onNewNumberAlertDialog = new BaseAlertDialog(getContext(), R.string.dialog_title_add_blockList_item, items) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // from contact
                        showNumberFromContact();
                        break;
                    case 1: // From Call Logs

                        showNumberSelectorFromCallLog();
                        break;
                    case 2:  // From message
                        showNumberFromSMS();
                        break;
                    case 3:  // from Custom
                        // show custom number input dialog.
                        showCustomNumberSelector();
                        break;
                }
            }
        };

        loadItems();
    }

    private void showNumberFromContact() {

        ContactListDialogFragment smsLogListDialogFragment = new ContactListDialogFragment();
        smsLogListDialogFragment.show(MyApp.getMainActivity().getSupportFragmentManager(), "contact");
    }

    private void showNumberFromSMS() {

        SMSLogListDialogFragment smsLogListDialogFragment = new SMSLogListDialogFragment();
        smsLogListDialogFragment.show(MyApp.getMainActivity().getSupportFragmentManager(), "sms");
    }

    private void loadItems() {
        try {
            items = db.getAllBlackListedNumbers();
            if (items.size() > 0) {
                textViewEmpty.setVisibility(View.GONE);
            }else {
                textViewEmpty.setVisibility(View.VISIBLE);
            }
            MyLogger.debug("Call logs count " + items.size());
            mAdapter = new BlockedNumberAdapter(items, R.layout.list_item_black_listed_numbers);
            mAdapter.setMyEventListener(this);
            mRecyclerView.setAdapter(mAdapter);

        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }
    }

    private void showCustomNumberSelector() {
        CustomPhoneNumberDialog callLogListFragment = new CustomPhoneNumberDialog();
        callLogListFragment.show(MyApp.getMainActivity().getSupportFragmentManager(), "custom_number");
        //callLogListFragment.setCallLogClicked(this);
    }


    private void showNumberSelectorFromCallLog() {
        CallLogListDialogFragment callLogListFragment = new CallLogListDialogFragment();
        callLogListFragment.show(MyApp.getMainActivity().getSupportFragmentManager(), "show_number_select");
        //callLogListFragment.setCallLogClicked(this);
    }

    @Override
    public void backPressedInFragment() {
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(true));
    }




    @Override
    public void onItemClick(int position, View v) {
        if (!isCabActive) {
            MyLogger.debug("onItemClick in  " + getClass().getName());
            blockedCallSMSNumber = mAdapter.items.get(position);
            onItemClickedAlertDialog.showDialog();
            // show Alert Dialog
        } else {
            BlockedCallSMSNumber item = mAdapter.items.get(position);
            mAdapter.toggleSelection(position);
            actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
            if (mAdapter.selectedItems.size() == 0) {
                EventBus.getDefault().post(new OnActionBarTitleChange("Blocked Number List"));
                isCabActive = false;
                makeBottomMenuAdd();
            }
        }
    }
    @OnClick(R.id.menu_action_clear)
    public void menuActionClear(View view) {
        MyLogger.debug("Clearning Add Image to Vault");
        mAdapter.clearSelections();
        actionbar_selected_items_count.setText(String.valueOf(0));
        EventBus.getDefault().post(new OnActionBarTitleChange("Blocked Number List"));
        isCabActive = false;
        makeBottomMenuAdd();
    }
    @Override
    public void onItemLongClick(int position, View v) {
        isCabActive = true;
        BlockedCallSMSNumber item = mAdapter.items.get(position);
        mAdapter.toggleSelection(position);
        actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
        bottomMenu.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new OnActionBarTitleChange("Delete Blocked Number"));
        makeBottomMenuMultipleSelect();
    }

    @Override
    public void onItemCheckedChange(int position, CompoundButton button, boolean checked) {
        BlockedCallSMSNumber item = items.get(position);
        switch (button.getId()) {
            case R.id.checkBoxBlockCall:
                break;
            case R.id.checkBoxBlockSMS:
                break;
        }
    }
}

package com.callsms.logs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.callsms.calls.BlockedCallSMSNumber;
import com.callsms.calls.CallLogPojo;
import com.callsms.calls.OnRefreshBlockedNumberList;
import com.callsms.sms.SMSLogAdapter;
import com.callsms.sms.SMSLogHelper;
import com.callsms.sms.SmsPojo;
import com.fs.lib.util.BaseDialogFragment;
import com.fs.lib.util.MessageHelper;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.ViewHelper;
import com.mobiguard.MyApp;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.passwords.password.OnRefreshPasswords;
import com.util.MyDatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * CallLogList fragment is a service that shows call log with an interface.
 */
public class SMSLogListDialogFragment extends BaseDialogFragment implements MyEventListener, View.OnClickListener {
    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.bottomMenu)
    Toolbar bottomMenu;

    @Bind(R.id.checkBoxBlockCall)
    CheckBox checkBoxBlockCall;

    @Bind(R.id.checkBoxBlockSMS)
    CheckBox checkBoxBlockSMS;


    private List<SmsPojo> items = new ArrayList<>();
    private MyDatabaseHelper db;
    private SMSLogAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentManager fragmentManager;
    private boolean isBusy=false;
    private boolean isLocked=false;
    ImageView imageViewBack;
    ImageView imageViewOk;
    TextView actionbar_selected_items_count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_call_log_list, null);
        ButterKnife.bind(this, view);
        List<CallLogPojo> callLogs= CallLogsHelper.getCallDetails(MyApp.getMainActivity());
        MyLogger.debug("Call logs count " + callLogs.size());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        loadItems();
        getDialog().setTitle("Select SMS Logs");
        imageViewBack = (ImageView) bottomMenu.findViewById(R.id.imageViewBack);
        imageViewOk = (ImageView) bottomMenu.findViewById(R.id.imageViewOk);
        actionbar_selected_items_count = (TextView) bottomMenu.findViewById(R.id.actionbar_selected_items_count);
        imageViewBack.setOnClickListener(this);
        imageViewOk.setOnClickListener(this);

        ViewHelper.addHover(imageViewBack);
        ViewHelper.addHover(imageViewOk);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnActionBarTitleChange("Call Log List"));
//        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
    }


    private void loadItems() {
        EventBus.getDefault().post(new OnRefreshPasswords());
        try {
             items= SMSLogHelper.getAllSms(MyApp.getMainActivity());
            MyLogger.debug("sms logs count " + items.size());
            if (items.size()>0) {
                mAdapter = new SMSLogAdapter(items,R.layout.list_item_sms_log_list);
                mAdapter.setMyEventListener(this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Crouton.showText(MyApp.getMainActivity(),"No Call Log Found.", Style.INFO);
            }

        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(int position, View v) {
        if(!isBusy) {
            isBusy=true;
            SmsPojo item=mAdapter.items.get(position);
            mAdapter.toggleSelection(position);
            actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
            isBusy=false;
        }
    }

    @Override
    public void onItemLongClick(int position, View v) {

    }

    @Override
    public void onItemCheckedChange(int position,CompoundButton button, boolean checked) {

    }

    @Override
    public void onClick(View v) {
        if(!isBusy) {
            isBusy=true;

        switch (v.getId()){
            case R.id.imageViewBack:
                    MyLogger.debug("back clicked");
                getDialog().cancel();
                break;

            case R.id.imageViewOk:
                for(Map.Entry<Integer, Boolean> entry:mAdapter.selectedItems.entrySet()){
                    // get each number
                    SmsPojo itemPojo=items.get(entry.getKey().intValue());
                    BlockedCallSMSNumber blackListedNumbers=new BlockedCallSMSNumber();
                    try {

                            if (checkBoxBlockCall.isChecked()) {
                                blackListedNumbers.setIsCallBlocked(true);
                            }

                            if (checkBoxBlockSMS.isChecked()) {
                                blackListedNumbers.setIsSMSBlocked(true);
                            }

                        blackListedNumbers.setBlockedNumber(itemPojo.getAddress());
                        db.getBlackListedCallSMSNumberDao().create(blackListedNumbers);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(mAdapter.selectedItems.size()>0){
                    MessageHelper.showInfo(MyApp.getMainActivity(), "Number Added to Black List.");
                }
                getDialog().cancel();


                break;
        }
        }
        isBusy=false;
        EventBus.getDefault().post(new OnRefreshBlockedNumberList());
    }
}

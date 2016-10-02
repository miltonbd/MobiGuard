package com.callsms.calls;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fs.lib.util.BaseDialogFragment;
import com.fs.lib.util.MessageHelper;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.ViewHelper;
import com.mobiguard.MyApp;
import com.mobiguard.R;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by milton on 21/02/16.
 */
public class CustomPhoneNumberDialog extends BaseDialogFragment {
    private MyDatabaseHelper db;

    @Bind(R.id.buttonOk)
    View buttonOk;

    @Bind(R.id.buttonCancel)
    View buttonCancel;

    @Bind(R.id.editTextCustomPhoneNumber)
    EditText editTextCustomPhoneNumber;

    @Bind(R.id.checkBoxBlockCall)
    CheckBox checkBoxBlockCall;

    @Bind(R.id.checkBoxBlockSMS)
    CheckBox checkBoxBlockSMS;

    private BlockedCallSMSNumber blockedCallSMSNumber;

    public static CustomPhoneNumberDialog getInstance(BlockedCallSMSNumber blockedCallSMSNumber) {
        CustomPhoneNumberDialog instance = new CustomPhoneNumberDialog();
        instance.setBlockedCallSMSNumber(blockedCallSMSNumber);
        return instance;
    }

    public BlockedCallSMSNumber getBlockedCallSMSNumber() {
        return blockedCallSMSNumber;
    }

    public void setBlockedCallSMSNumber(BlockedCallSMSNumber blockedCallSMSNumber) {
        this.blockedCallSMSNumber = blockedCallSMSNumber;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_custom_phone_number_selector, null);
        ButterKnife.bind(this, view);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        if (blockedCallSMSNumber == null) {
            getDialog().setTitle("Enter Custom Phone Number");
        } else {
            getDialog().setTitle("Edit Phone Number");
            editTextCustomPhoneNumber.setText(blockedCallSMSNumber.getBlockedNumber());
            checkBoxBlockCall.setChecked(blockedCallSMSNumber.getIsCallBlocked());
            checkBoxBlockSMS.setChecked(blockedCallSMSNumber.getIsSMSBlocked());
        }
        return view;
    }

    @OnClick(R.id.buttonOk)
    public void onClickOk(View v) {
        String phoneNumber = editTextCustomPhoneNumber.getText().toString();
        if (phoneNumber.matches("")) {
            MyLogger.show(getContext(), "Phone Number Can Not be Empty");
            return;
        }
        try {
            if (blockedCallSMSNumber == null) { // Add item
                BlockedCallSMSNumber blackListedNumber = new BlockedCallSMSNumber();
                blackListedNumber.setBlockedNumber(phoneNumber);
                blackListedNumber.setIsCallBlocked(checkBoxBlockCall.isChecked());
                blackListedNumber.setIsSMSBlocked(checkBoxBlockSMS.isChecked());
                db.getBlackListedCallSMSNumberDao().create(blackListedNumber);
                MessageHelper.showToast(getContext(), "Number Created Successfully");

            } else { // Edit item
                UpdateBuilder<BlockedCallSMSNumber, Long> ub = db.getBlackListedCallSMSNumberDao().updateBuilder();
                ub.updateColumnValue(MyDatabaseHelper.blockedNumber, phoneNumber);
                ub.updateColumnValue(MyDatabaseHelper.isCallBlocked, checkBoxBlockCall.isChecked());
                ub.updateColumnValue(MyDatabaseHelper.isSMSBlocked, checkBoxBlockSMS.isChecked());
                ub.where().eq(MyDatabaseHelper.id,blockedCallSMSNumber.getId());
                ub.update();
                MessageHelper.showToast(getContext(), "Number Edited Successfully");
            }


            MessageHelper.showInfo(MyApp.getMainActivity(), "Phone Number Added to Black List.");
            EventBus.getDefault().post(new OnRefreshBlockedNumberList());


        } catch (SQLException e) {
            e.printStackTrace();
        }

        getDialog().cancel();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ViewHelper.hideKeyboard(editTextCustomPhoneNumber);
    }

    @OnClick(R.id.buttonCancel)
    public void onClickCancel(View v) {
        getDialog().cancel();
        ViewHelper.hideKeyboard(editTextCustomPhoneNumber);
    }
}

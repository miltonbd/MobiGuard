package com.callsms.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.callsms.logs.HideLogs;
import com.fs.lib.util.DateTimeHelper;
import com.fs.lib.util.MyLogger;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.dao.Dao;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class IncomingCallReceiver extends BroadcastReceiver {

    public void onReceive( Context context, Intent intent) {
        MyDatabaseHelper db=MyDatabaseHelper.getInstance(context);
        Bundle bundle = intent.getExtras();
        String phoneNr= bundle.getString("incoming_number");
        MyLogger.debug("Incoming phone number "+phoneNr);
        // test if the number is callblocked list.
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Toast.makeText(context, "Phone Is Ringing", Toast.LENGTH_LONG).show();
            try {
                if (db.isNumberInBlackListForIncomingCall(phoneNr)) {
                    MyLogger.debug("Number is in Block List");
                    declinePhone(context);
                    // save the Call to BlockedCalls table.
                    BlockedCalls blockedCalls = new BlockedCalls();
                    blockedCalls.setBlockedNumber(phoneNr);
                    blockedCalls.setDateTime(DateTimeHelper.formateDateTime());
                    Dao<BlockedCalls, Long> dao = db.getBlockedCallDao();
                    dao.create(blockedCalls);
                    HideLogs.deleteAnEntryFromCallLog(context, phoneNr);
                } else {
                    MyLogger.debug("Number is not in Block List");
                }
            } catch (SQLException e) {
                MyLogger.debug("SQL Exception " + e.getMessage() + " " + getClass().getName());
                e.printStackTrace();
            } catch (Exception e) {
                MyLogger.debug("Exeption " + e.getMessage() + " " + getClass().getName());
                e.printStackTrace();
            }

        }
    }

    private void declinePhone(Context context) throws Exception {

        try {

            String serviceManagerName = "android.os.ServiceManager";
            String serviceManagerNativeName = "android.os.ServiceManagerNative";
            String telephonyName = "com.android.internal.telephony.ITelephony";
            Class<?> telephonyClass;
            Class<?> telephonyStubClass;
            Class<?> serviceManagerClass;
            Class<?> serviceManagerNativeClass;
            Method telephonyEndCall;
            Object telephonyObject;
            Object serviceManagerObject;
            telephonyClass = Class.forName(telephonyName);
            telephonyStubClass = telephonyClass.getClasses()[0];
            serviceManagerClass = Class.forName(serviceManagerName);
            serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
            Method getService = // getDefaults[29];
                    serviceManagerClass.getMethod("getService", String.class);
            Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
            Binder tmpBinder = new Binder();
            tmpBinder.attachInterface(null, "fake");
            serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
            IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
            Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
            telephonyObject = serviceMethod.invoke(null, retbinder);
            telephonyEndCall = telephonyClass.getMethod("endCall");
            telephonyEndCall.invoke(telephonyObject);

        } catch (Exception e) {
            MyLogger.debug("msg cant dissconect call...."+e.getMessage());
            e.printStackTrace();


        }

    }
}


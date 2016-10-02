package com.callsms.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.fs.lib.util.DateTimeHelper;
import com.fs.lib.util.MyLogger;
import com.util.MyDatabaseHelper;


public class SMSIncomingBroadCastReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        MyLogger.debug("SMS Received.");
        MyDatabaseHelper db=MyDatabaseHelper.getInstance(context);
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    if (db.isNumberInBlackListForIncomingSMS(senderNum)) {
                        MyLogger.debug("SMS Received is Block List.");
                        BlockedSMS blockedSMS=new BlockedSMS();

                        blockedSMS.setBlockedNumber(phoneNumber);
                        blockedSMS.setSms(message);
                        blockedSMS.setDateTime(DateTimeHelper.formateDateTime());
                        db.getBlockedSMSDao().create(blockedSMS);
                        abortBroadcast();
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
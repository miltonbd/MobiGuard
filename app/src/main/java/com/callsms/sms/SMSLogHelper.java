package com.callsms.sms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milton on 15/02/16.
 */
public class SMSLogHelper {
    public static List<SmsPojo> getAllSms(AppCompatActivity activity) {
        List<SmsPojo> lstSms = new ArrayList<SmsPojo>();
        SmsPojo objSms = new SmsPojo();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        activity.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new SmsPojo();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                String address = c.getString(c
                        .getColumnIndexOrThrow("address"));

                objSms.setAddress(address);
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setDateTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName(SMSLogType.INBOX);
                } else {
                    objSms.setFolderName(SMSLogType.SENT);
                }
                if (address!=null&&android.text.TextUtils.isDigitsOnly(address)){
                    lstSms.add(objSms);
                }
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }
}

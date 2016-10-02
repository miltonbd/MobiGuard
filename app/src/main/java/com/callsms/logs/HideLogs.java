package com.callsms.logs;

import android.content.Context;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

/**
 * Created by milton on 30/12/15.
 */
public class HideLogs {
    public static void deleteAnEntryFromCallLog(Context context,String number)
    {
        try
        {
            Uri CALLLOG_URI = Uri.parse("content://call_log/calls");
            context.getContentResolver().delete(CALLLOG_URI,CallLog.Calls.NUMBER +"=?",new String[]{number});
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

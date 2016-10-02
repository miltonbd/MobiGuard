package com.callsms.logs;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;

import com.callsms.calls.CallLogPojo;
import com.callsms.calls.CallLogPojoType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by milton on 15/02/16.
 */
public class CallLogsHelper {
    public static List<CallLogPojo> getCallDetails(AppCompatActivity activity) {

        List<CallLogPojo> callLogs=new ArrayList<>();
        Cursor managedCursor = activity.managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int numberLabel = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            CallLogPojo item=new CallLogPojo();
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);

            item.setPhoneNumber(phNumber);
            item.setDateTime(callDate+" "+callDayTime);
            item.setDuration(callDuration);

            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    item.setCallType(CallLogPojoType.DIALLED);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    item.setCallType(CallLogPojoType.RECEIVED);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    item.setCallType(CallLogPojoType.MISSED);
                    break;
            }
            callLogs.add(item);
        }
        managedCursor.close();
        return callLogs;

    }
    private static String getCallDetails(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            stringBuffer.append("\n----------------------------------");
        }
        cursor.close();
        return stringBuffer.toString();
    }
}

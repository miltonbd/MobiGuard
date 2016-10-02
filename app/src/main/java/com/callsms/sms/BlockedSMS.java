package com.callsms.sms;

import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by milton on 15/02/16.
 */
public class BlockedSMS extends TableBase {

    @DatabaseField
    private String blockedNumber;

    @DatabaseField
    private String dateTime;

    @DatabaseField
    private String sms;


    public String getBlockedNumber() {
        return blockedNumber;
    }

    public void setBlockedNumber(String blockedNumber) {
        this.blockedNumber = blockedNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}

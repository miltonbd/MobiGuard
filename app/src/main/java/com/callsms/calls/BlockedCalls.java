package com.callsms.calls;

import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by milton on 15/02/16.
 */
public class BlockedCalls extends TableBase {
    @DatabaseField
    private String blockedNumber;
    @DatabaseField
    private String dateTime;


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
}

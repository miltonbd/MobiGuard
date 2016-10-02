package com.callsms.calls;

import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

/**
 * This table will hold all call and sms blocked numbers. for saving the blocked calls and sms
 * refer to BlockedCalls and BlockedSMS class individually.
 */
public class BlockedCallSMSNumber extends TableBase {
    @DatabaseField
    private String blockedNumber;

    @DatabaseField
    private Boolean isCallBlocked=false;

    @DatabaseField
    private Boolean isSMSBlocked=false;

    public Boolean getIsSMSBlocked() {
        return isSMSBlocked;
    }

    public void setIsSMSBlocked(Boolean isSMSBlocked) {
        this.isSMSBlocked = isSMSBlocked;
    }

    public Boolean getIsCallBlocked() {
        return isCallBlocked;
    }

    public void setIsCallBlocked(Boolean isCallBlocked) {
        this.isCallBlocked = isCallBlocked;
    }

    public String getBlockedNumber() {
        return blockedNumber;
    }

    public void setBlockedNumber(String blockedNumber) {
        this.blockedNumber = blockedNumber;
    }

    @Override
    public String toString() {
        return "BlockedCallSMSNumber{" +
                "blockedNumber='" + blockedNumber + '\'' +
                ", isCallBlocked=" + isCallBlocked +
                ", isSMSBlocked=" + isSMSBlocked +
                "} " + super.toString();
    }
}

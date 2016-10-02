package com.callsms.calls;

import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by milton on 19/02/16.
 */
public class CallLogPojo extends TableBase {
    @DatabaseField
    public String phoneNumber;
    @DatabaseField
    public String dateTime;

    @DatabaseField
    public String duration;
    @DatabaseField
    public CallLogPojoType callType;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public CallLogPojoType getCallType() {
        return callType;
    }

    public void setCallType(CallLogPojoType callType) {
        this.callType = callType;
    }
}

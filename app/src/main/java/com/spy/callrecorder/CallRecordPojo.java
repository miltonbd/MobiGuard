package com.spy.callrecorder;


import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

public class CallRecordPojo extends TableBase {

    @DatabaseField
    private String dateTime;

    @DatabaseField
    private String filePath;

    @DatabaseField
    private Float duration;


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }
}


package com.callsms.sms;

/**
 * Created by milton on 18/02/16.
 */
public class SmsPojo {
    private String id;
    private String address;
    private String msg;
    private String readState;
    private String dateTime;
    private SMSLogType folderName;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getReadState() {
        return readState;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public SMSLogType getFolderName() {
        return folderName;
    }

    public void setFolderName(SMSLogType folderName) {
        this.folderName = folderName;
    }

}

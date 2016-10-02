package com.callsms;

/**
 * Created by milton on 28/02/16.
 */
public enum CallSMSBlockType {

    RejectBlackList("Reject Black List"),  RejectWhiteList("Reject White List"),
    AcceptWhiteList("Accept White List"),  AcceptBlackList("Accept Black List"),
    AcceptAll("Accept All"), RejectAll("Reject All");

    private String title;

    public String getTitle() {
        return title;
    }

    CallSMSBlockType(String title) {
        this.title = title;
    }
}

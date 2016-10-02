package com.callsms.calls;

/**
 * Created by milton on 19/02/16.
 */
public enum CallLogPojoType {
    MISSED("Missed"),RECEIVED("Receieved"),DIALLED("Dialled");
    CallLogPojoType(String type){
        this.type=type;
    }
    String type;

    public String getType() {
        return type;
    }
}

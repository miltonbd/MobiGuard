package com.mobiguard;

/**
 * Created by milton on 11/02/16.
 */
public class OnSetDrawerEnabledEvent {
    private boolean isEnabled;

    public OnSetDrawerEnabledEvent(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}

package com.passwords.events;

/**
 * Created by milton on 15/01/16.
 */
public class OnPasswordIconClicked {
    Integer icon;

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public OnPasswordIconClicked(Integer icon) {
        this.icon = icon;
    }
}

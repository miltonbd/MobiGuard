package com.applock;

import android.graphics.drawable.Drawable;

/**
 * Created by milton on 5/03/16.
 */
public class AppInfoPojo {
    private String appName;

    private String packageName;

    private int icon;

    private String versionName;

    private int versionCode;

    private Drawable iconDrawable;

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "AppInfoPojo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", icon=" + icon +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", iconDrawable=" + iconDrawable +
                '}';
    }
}

package com.applock;

import android.graphics.drawable.Drawable;

import com.util.MyDatabaseHelper;
import com.j256.ormlite.field.DatabaseField;



public class LockedApps  {

    @DatabaseField(generatedId = true,columnName = MyDatabaseHelper.id)
    private Long id;

    @DatabaseField
    private String appName;

    @DatabaseField
    private String packageName;

    @DatabaseField
    private int icon;

    @DatabaseField
    private String versionName;

    @DatabaseField
    private Integer versionCode;

    private Drawable iconDrawable;

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    @DatabaseField
    private boolean isUnLocked=false; // It should be true when the app is unlocked by unlock activity.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isUnLocked() {
        return isUnLocked;
    }

    public void setIsUnLocked(boolean isUnLocked) {
        this.isUnLocked = isUnLocked;
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

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "LockedApps{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", icon=" + icon +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", isUnLocked=" + isUnLocked +
                '}';
    }
}

package com.passwords.password;


import com.mobiguard.R;
import com.passwords.category.PasswordCategory;
import com.util.MyDatabaseHelper;
import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

public class Passwords extends TableBase {
    @DatabaseField(canBeNull = true,columnName = MyDatabaseHelper.icon)
    private int icon= R.drawable.icon_star;
    @DatabaseField(columnName = MyDatabaseHelper.title)
    private String title;

    @DatabaseField(canBeNull = false, foreign = true,foreignAutoRefresh=true,foreignAutoCreate=true,columnName = MyDatabaseHelper.passwordCategoryId)
    private PasswordCategory passwordCategory;

    @DatabaseField(canBeNull = true,columnName = MyDatabaseHelper.groupPosition)
    private int groupPosition;

    public PasswordCategory getPasswordCategory() {
        return passwordCategory;
    }

    public void setPasswordCategory(PasswordCategory passwordCategory) {
        this.passwordCategory = passwordCategory;
    }
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    @Override
    public String toString() {
        return "Passwords{" +
                "icon=" + icon +
                ", title='" + title + '\'' +
                ", passwordCategory=" + passwordCategory +
                "} " + super.toString();
    }
}

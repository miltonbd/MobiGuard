package com.passwords.category;

import com.util.TableBase;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.field.DatabaseField;

public class PasswordCategory extends TableBase {

    @DatabaseField(columnName = MyDatabaseHelper.name)
    private String name;

    @DatabaseField(canBeNull = true,columnName = MyDatabaseHelper.icon)
    private Long icon;

    public Long getIcon() {
        return icon;
    }

    public void setIcon(Long icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static PasswordCategory buildCategory(String name, int icon) {
        PasswordCategory passwordCategory=new PasswordCategory();
        passwordCategory.setName(name);
        passwordCategory.setIcon(Long.valueOf(icon));
        return passwordCategory;
    }

    @Override
    public String toString() {
        return "PasswordCategory{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                "} " + super.toString();
    }
}

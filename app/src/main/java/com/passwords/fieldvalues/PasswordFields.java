package com.passwords.fieldvalues;


import com.passwords.category.PasswordCategory;
import com.passwords.password.Passwords;
import com.util.MyDatabaseHelper;
import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

public class PasswordFields extends TableBase {

    @DatabaseField(columnName = MyDatabaseHelper.isCustom)
    boolean isCustom=false;

    @DatabaseField(columnName = MyDatabaseHelper.name)
    String name;

    @DatabaseField(canBeNull = true, foreign = true,columnName = MyDatabaseHelper.passwordCategoryId)
    PasswordCategory passwordCategory;

    @DatabaseField(canBeNull = true, foreign = true,foreignAutoRefresh=true,foreignAutoCreate=true,columnName = MyDatabaseHelper.passwordId)
    Passwords passwords;

    PasswordFieldValues fieldValue;

    public boolean isCustom() {
        return isCustom;
    }

    public void setIsCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public PasswordFieldValues getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(PasswordFieldValues fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Passwords getPassword() {
        return passwords;
    }

    public void setPassword(Passwords password) {
        this.passwords = password;
    }

    public PasswordCategory getPasswordCategory() {
        return passwordCategory;
    }

    public void setPasswordCategory(PasswordCategory passwordCategory) {
        this.passwordCategory = passwordCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PasswordFields{" +
                "name='" + name + '\'' +
                ", passwordCategory=" + passwordCategory +
                ", passwords=" + passwords +
                "} " + super.toString();
    }


}

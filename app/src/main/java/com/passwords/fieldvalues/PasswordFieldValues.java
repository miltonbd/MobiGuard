package com.passwords.fieldvalues;

import com.crypto.CryptoHelper;
import com.passwords.category.PasswordCategory;
import com.passwords.password.Passwords;
import com.util.MyDatabaseHelper;
import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

public class PasswordFieldValues extends TableBase {

    @DatabaseField(columnName = MyDatabaseHelper.value)
    private String value;

    @DatabaseField(canBeNull = true,foreign = true,foreignAutoRefresh=true,foreignAutoCreate=true,columnName = MyDatabaseHelper.passwordId)
    private Passwords password;

    @DatabaseField(canBeNull = true,foreign = true,foreignAutoRefresh=true,foreignAutoCreate=true,columnName = MyDatabaseHelper.passwordFieldsId)
    private PasswordFields passwordField;

    @DatabaseField(canBeNull = true,foreign = true,foreignAutoRefresh=true,foreignAutoCreate=true,columnName = MyDatabaseHelper.passwordCategoryId)
    private PasswordCategory passwordCategory;


    public PasswordCategory getPasswordCategory() {
        return passwordCategory;
    }

    public void setPasswordCategory(PasswordCategory passwordCategory) {
        this.passwordCategory = passwordCategory;
    }
    public String getValue() {
        CryptoHelper cryptoHelper=CryptoHelper.getInstannce();
        cryptoHelper.decryptFields(this);
        return value;
    }

    public void setValue(String value) {
        this.value=value;
        CryptoHelper cryptoHelper=CryptoHelper.getInstannce();
        cryptoHelper.encryptFields(this);
    }


    public Passwords getPassword() {
        return password;
    }

    public void setPassword(Passwords password) {
        this.password = password;
    }

    public PasswordFields getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(PasswordFields passwordField) {
        this.passwordField = passwordField;
    }
}

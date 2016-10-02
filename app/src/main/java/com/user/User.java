package com.user;

import com.util.MyDatabaseHelper;
import com.j256.ormlite.field.DatabaseField;

/*
It may hold user details like name, profile picture, contact details.
 */
public class User {

    @DatabaseField(generatedId = true,columnName = MyDatabaseHelper.id)
    private Long id;

    @DatabaseField
    private String password;

    @DatabaseField
    private boolean isLoggedIn=false;

    @DatabaseField
    private boolean isUserExists=false;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isUserExists() {
        return isUserExists;
    }

    public void setIsUserExists(boolean isUserExists) {
        this.isUserExists = isUserExists;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

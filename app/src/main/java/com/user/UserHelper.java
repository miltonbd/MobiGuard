package com.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.crypto.Crypto;
import com.mobiguard.MyApp;
import com.util.MyDatabaseHelper;
import com.fs.lib.util.MyLogger;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

/**
 * Created by milton on 19/12/15.
 */
public class UserHelper {
    private static UserHelper instance;
    private static MyDatabaseHelper db;
    public static long defaultUserId = 1;
    private static Context context;

    public static UserHelper getInstance(Context context1) {
        if (instance == null) {
            instance = new UserHelper();
        }
        db = MyDatabaseHelper.getInstance(context1);
        context = context1;
        return instance;
    }

    public void loginUser(String passwordLogin) {
        try {
            Dao<User, Long> userDao = db.getDao(User.class);
            User userInDb = userDao.queryForId(defaultUserId);
            String hashedLogin = new String(Crypto.MD5(passwordLogin));
            if (hashedLogin.matches(userInDb.getPassword())) {
                UpdateBuilder<User, Long> updateBuilder = userDao.updateBuilder();
                updateBuilder.updateColumnValue("isLoggedIn", true);
                updateBuilder.where().eq("id", userInDb.getId());
                updateBuilder.update();
            } else {
                MyLogger.show(context, "Wrong Passwords");

            }

        } catch (Exception e) {
            MyLogger.show(context, e.getMessage());
        }
    }

    public void logoutUser() {
        try {
            Dao<User, Long> userDao = db.getDao(User.class);
            User userInDb = userDao.queryForId(defaultUserId);
            UpdateBuilder<User, Long> updateBuilder = userDao.updateBuilder();
            updateBuilder.updateColumnValue("isLoggedIn", false);
            updateBuilder.where().eq("id", userInDb.getId());
            updateBuilder.update();
        } catch (Exception e) {
            MyLogger.show(context, e.getMessage());
        }
    }

    public static String getMD5String(String inputString) {
        return new String(Crypto.MD5(inputString));
    }

    public static byte[] getMD5Byte(String inputString) {
        return Crypto.MD5(inputString);
    }

    public static String getHashedPasswordAsString(String password) {
        return new String(Base64.encode(UserHelper.getMD5Byte(password), Base64.DEFAULT));
    }

    public User getCurrentUser() {
        try {
            QueryBuilder<User, Long> qb = db.getUserDao().queryBuilder();
            qb.where().eq("id", MyDatabaseHelper.defaultUserId);
            return qb.queryForFirst();
        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
        }
        return null;
    }


    public User getLoggedUser() {
        try {
            QueryBuilder<User, Long> qb = db.getUserDao().queryBuilder();
            qb.where().eq("id", MyDatabaseHelper.defaultUserId);
            qb.where().eq("isLoggedIn", true);
            return qb.queryForFirst();
        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
        }
        return null;
    }

    public void createNewUser(String passwordSignup) throws SQLException {
        User userSignup = new User();
        userSignup.setId(MyDatabaseHelper.defaultUserId);
        userSignup.setIsUserExists(true);
        userSignup.setPassword(UserHelper.getHashedPasswordAsString(passwordSignup));

        MyDatabaseHelper db = MyDatabaseHelper.getInstance(MyApp.getContext());
        Dao<User, Long> dao = db.getDao(User.class);
        dao.create(userSignup);
        //MyLogger.show(MyApp.getContext(), " User Created.");

    }

    public boolean isLoggedIn() {
        try {
            Dao<User, Long> userDao = db.getDao(User.class);
            QueryBuilder<User, Long> qb = userDao.queryBuilder();
            qb.where().eq("id", MyDatabaseHelper.defaultUserId);
            qb.where().eq("isLoggedIn", true);
            return qb.query().size() == 1;
        } catch (Exception e) {
            MyLogger.show(context, e.getMessage());
        }

        return false;
    }

    public void returnToLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public void sendToLoginIfNotLoggedIn(AppCompatActivity activity) {

        if (!isLoggedIn()) {
            activity.finish();
            activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
        }
    }

    public static void makeUserLoggedIn(MyDatabaseHelper db) {
        Dao<User, Long> userDao = null;
        try {
            userDao = db.getDao(User.class);
            User userInDb = userDao.queryForId(MyDatabaseHelper.defaultUserId);
            userInDb.setIsLoggedIn(true);
            UpdateBuilder<User, Long> updateBuilder = userDao.updateBuilder();
            updateBuilder.updateColumnValue("isLoggedIn", true);
            updateBuilder.where().eq("id", userInDb.getId());
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

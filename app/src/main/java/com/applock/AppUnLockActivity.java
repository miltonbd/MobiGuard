package com.applock;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.user.User;
import com.user.UserHelper;
import com.util.MyDatabaseHelper;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AppUnLockActivity extends AppCompatActivity implements TextWatcher {
    private long id;
    private long defaultUserId = 1;
    private Context cx;
    private ActivityManager am;
    private AppLockHelper appLockHelper;
    private int MAX_ATTEMPT = 3;
    MyDatabaseHelper db;
    @Bind(R.id.editTextLoginPassword)
    EditText editTextLoginPassword;

    @Bind(R.id.buttonLoginPassword)
    Button buttonLoginPassword;

    @Bind(R.id.textViewAppName)
    TextView textViewAppName;

    //@Bind(R.id.gridViewNumKeypad)
    GridView gridViewNumKeypad;

    @Bind(R.id.imageViewAppIcon)
    ImageView imageViewAppIcon;
    private LockedApps currentApp;
   private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_un_lock);
        cx = getApplicationContext();
        am = (ActivityManager) cx.getSystemService(Context.ACTIVITY_SERVICE);
        appLockHelper = new AppLockHelper(am);
        ButterKnife.bind(this);
        db = MyDatabaseHelper.getInstance(getApplicationContext());
        id = getIntent().getLongExtra("id", 0);
        try {
            currentApp= db.getCurrentLockedApp(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        editTextLoginPassword.addTextChangedListener(this);
        String appName= getIntent().getStringExtra("appName");
        textViewAppName.setText(appName);
        Drawable icon = null;
        try {

            packageName = getIntent().getStringExtra("package");
            icon = cx.getPackageManager().getApplicationIcon(packageName);
            imageViewAppIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //NumKeypadAdapter numKeypadAdapter=new NumKeypadAdapter(getApplicationContext(),R.layout.list_item_num_keypad,Arrays.asList(NUM_KEYS.values()));
        //gridViewNumKeypad.setAdapter(numKeypadAdapter);
    }

    @Override
    public void onBackPressed() {
        //returnToHome();
    }

    private void returnToHome() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @OnClick(R.id.buttonLoginPassword)
    public void buttonLoginPassword() {
        String passwordLogin = new String(UserHelper.getHashedPasswordAsString(editTextLoginPassword.getText().toString()));
        try {
            Dao<User, Long> userDao = db.getDao(User.class);
            User userInDb = userDao.queryForId(defaultUserId);
            if (passwordLogin.matches(userInDb.getPassword())) {
                db.unlockApp(packageName);
                EventBus.getDefault().post(new OnHasUnLockedApps(true,currentApp));
                finish();
            } else {
                MyLogger.show(getApplicationContext(), "Wrong Passwords");

            }

        } catch (Exception e) {
            MyLogger.debug("buttonLoginPassword exception "+e.getMessage());
            e.getStackTrace();
        }
        killPackage();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String passwordLogin = new String(UserHelper.getHashedPasswordAsString(editTextLoginPassword.getText().toString()));
        try {
            Dao<User, Long> userDao = db.getDao(User.class);
            User userInDb = userDao.queryForId(defaultUserId);
            if (passwordLogin.matches(userInDb.getPassword())) {
                db.unlockApp(packageName);
                EventBus.getDefault().post(new OnHasUnLockedApps(true,currentApp));
                finish();
            }
        } catch (Exception e) {
            MyLogger.debug("buttonLoginPassword exception "+e.getMessage());
            e.getStackTrace();
        }
        killPackage();
    }

    private void killPackage() {
        int pId = appLockHelper.findPIDbyPackageName(packageName);
        if (appLockHelper.isPackageRunning(packageName)) {
            appLockHelper.killPackageProcesses(packageName);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}

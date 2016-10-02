package com.mobiguard;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.applock.ListAppLockFragment;
import com.callsms.BlockedCallSMSContainerFragment;
import com.crypto.Crypto;
import com.applock.ListAppLockContainerFragment;
import com.images.ListLockedImageFileFragment;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;
import com.passwords.PasswordContainerFragment;
import com.passwords.events.OnBackPressed;
import com.spy.SpyContainerFragment;
import com.user.LoginActivity;
import com.user.UserHelper;
import com.util.MyDatabaseHelper;
import com.videos.ListLockedVideoFileFragment;

import de.greenrobot.event.EventBus;

import java.util.Stack;

public abstract class BaseMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public RelativeLayout main_content;
    public Stack<Fragment> fragmentStack=new Stack<Fragment>();
    public ActionBar actionBar;
    public MyDatabaseHelper db;
    public UserHelper userHelper;
    public FragmentManager supportFragmentManager;
    public DashboardFragment dashboardFragment;
    public PasswordContainerFragment passwordsContainerFragment;
    public ListAppLockFragment appLockFragment;
    public ListLockedVideoFileFragment videosLockFragment;
    public ListLockedImageFileFragment picturesLockFragment;
    public SettingFragment settingFragment;
    public Toolbar toolbar;
    public int autoLogoutTime = 1 * 60 * 1000 * 1000;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public Crypto crypto;
    public String password;
    public VaultAsyncTask vaultASyncTask;
    public boolean hasVaultOperations = true; // will escape onEncrypt and onDecrypt
    public DrawerLayout mDrawerLayout;
    public NavigationView navigationView;
    CountDownTimer timer = new CountDownTimer(autoLogoutTime, 1000) {

        public void onTick(long millisUntilFinished) {
            //Some code
        }

        public void onFinish() {
            // todo show dialog to logout.
            userHelper.logoutUser();
            userHelper.returnToLogin();
            finish();
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.y<
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private SpyContainerFragment spyContainerFragment;
    private BlockedCallSMSContainerFragment callSmsFragment;
    private ListAppLockContainerFragment appLockContainerFragment;

    // timer.start();
    //attachLockedPictureFragment();
    //attachLockedVideoFragment();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    public void init() {
        main_content= (RelativeLayout) findViewById(R.id.main_content);
        db = MyDatabaseHelper.getInstance(getApplicationContext());
      /*  try {
            db.createSampleData();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        supportFragmentManager = getSupportFragmentManager();
        dashboardFragment = DashboardFragment.newInstance();
        passwordsContainerFragment = new PasswordContainerFragment();
        appLockContainerFragment = new ListAppLockContainerFragment();
        appLockFragment=new ListAppLockFragment();
        videosLockFragment = new ListLockedVideoFileFragment();
        picturesLockFragment = new ListLockedImageFileFragment();
        callSmsFragment = new BlockedCallSMSContainerFragment();
        spyContainerFragment = new SpyContainerFragment();
        settingFragment = new SettingFragment();
        showNavigationDrawer();
    }

    private void showNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.syncState();

        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLogger.debug("Back Pressed in actionBarDrawerToggle");
                   EventBus.getDefault().post(new OnBackPressed());
                }
            });
            actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_ab_back_holo_dark_am);
            actionBarDrawerToggle.syncState();
        }
    }

    public void startLogInActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

   /* public void attachSettingFragment() {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_content, settingFragment).addToBackStack(Statics.FRAGMENT_TAG_SETTING).commit();
    }*/

    public void attachLockedAppsContainerFragment() {
        EventBus.getDefault().post(new OnAttachFragment(appLockContainerFragment));
    }

    public void attachLockedAppsFragment() {
        EventBus.getDefault().post(new OnAttachFragment(appLockFragment));
    }

    public void attachFragment(Fragment fragment) {
       // main_content.removeAllViews();
        fragmentStack.push(fragment);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        // make tag using position
        supportFragmentManager.beginTransaction().
                replace(R.id.main_content, fragment).commit();
    }


    public void attachSpyContaianerFragment() {
        EventBus.getDefault().post(new OnAttachFragment(spyContainerFragment));
    }


    public void attachLockedCallsSms() {
        EventBus.getDefault().post(new OnAttachFragment(callSmsFragment));
    }

    public void attachLockedVideoFragment() {
        EventBus.getDefault().post(new OnAttachFragment(videosLockFragment));
    }

    public void attachLockedPictureFragment() {
        EventBus.getDefault().post(new OnAttachFragment(picturesLockFragment));
    }

    public void attachPasswordContainerFragment() {
        EventBus.getDefault().post(new OnAttachFragment(passwordsContainerFragment));
    }

    public void attachDashboardFragment() {
        EventBus.getDefault().post(new OnAttachFragment(dashboardFragment));
    }

    public void attachListLockedAppFragment() {
        EventBus.getDefault().post(new OnAttachFragment(appLockFragment));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.dashboardDrawer:
                MyLogger.debug("Navigation item Dashboard");
                attachDashboardFragment();
                EventBus.getDefault().post(new OnCheckDrawerPosition(0));
                break;
            case R.id.passwordsDrawer:
                attachPasswordContainerFragment();
                EventBus.getDefault().post(new OnCheckDrawerPosition(1));
                break;
            case R.id.picturesDrawer:
                attachLockedPictureFragment();
                EventBus.getDefault().post(new OnCheckDrawerPosition(2));
                break;
            case R.id.videosDrawer:
                attachLockedVideoFragment();
                EventBus.getDefault().post(new OnCheckDrawerPosition(3));
                break;
            case R.id.appslockdDrawer:
                attachLockedAppsFragment();
                EventBus.getDefault().post(new OnCheckDrawerPosition(4));
                break;

            case R.id.callSmslockdDrawer:
                attachLockedCallsSms();
                EventBus.getDefault().post(new OnCheckDrawerPosition(5));
                break;

            case R.id.spyDrawer:
             //  attachSpyContaianerFragment();
                rateApp();
                EventBus.getDefault().post(new OnCheckDrawerPosition(6));

                break;
         /*   case R.id.settingdDrawer:
                attachSettingFragment();
                EventBus.getDefault().post(new OnCheckDrawerPosition(5));
                break;*/
            case R.id.logoutDrawer:
                userHelper.logoutUser();
                finish();
                startLogInActivity();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("http://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }
    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

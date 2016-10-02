package com.mobiguard;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.crypto.Crypto;
import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;
import com.passwords.category.OnAttachPasswordEditFragment;
import com.passwords.events.OnBackPressed;
import com.user.User;
import com.user.UserHelper;
import com.util.BUILD_TYPES;
import com.util.MyDatabaseHelper;
import com.util.Statics;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseMainActivity {

    public void onEvent(OnUserActivityEvent event) {
        timer.start();
    }

    public void onEvent(OnHasVaultOperationsEvent event) {
        hasVaultOperations = event.isHasVaultOperations();
    }

    public void onEvent(OnActionBarTitleChange event) {
        MyApp.getMainActivity().getSupportActionBar().setTitle(event.getTitle());
    }

    public void onEvent(OnBackPressed event) {
        onBackPressed();
    }

    public void onEvent(OnAttachPasswordEditFragment event) {
        attachFragment(event.getAddPasswordCategoryFragment());
    }

    public void onEvent(OnSetDrawerEnabledEvent event) {
        if (event.isEnabled()) {
            setDrawerState(true);
        } else {
            setDrawerState(false);
        }
    }

    public void onEvent(OnAttachFragment event) {
        attachFragment(event.getFragment());
    }

    public void onEvent(OnRemoveFragmentFromBackStack event) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MyLogger.debug("total items in backstack before " + supportFragmentManager.getBackStackEntryCount());
        // supportFragmentManager.beginTransaction().remove().commit();
        MyLogger.debug("total items in backstack after " + supportFragmentManager.getBackStackEntryCount());

    }

    @Override
    public void onUserInteraction() {
        // timer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        MyApp.setMainActivity(this);
        MyLogger.debug("Creating " + this.getLocalClassName());
        init();
        userHelper = UserHelper.getInstance(getApplicationContext());
        User loggedUser = userHelper.getLoggedUser();
        if (loggedUser != null) {
            password = loggedUser.getPassword();
        } else {
            password = MyDatabaseHelper.testPassword;
            // start Login Activity
            if (Statics.buildType== BUILD_TYPES.RELEASE){
                startLogInActivity();
            }else {
                // Make dummy Login

            }
        }

        MyLogger.debug("user password " + password);
        vaultASyncTask = new VaultAsyncTask(getApplicationContext());
        actionBar = getSupportActionBar();
        attachDashboardFragment();
     //   attachSpyContaianerFragment();
    }

    public void testEncryptionWithSalt() {
        String dat="Test Data";
        String salt="salt";
        Crypto crypto=Crypto.getCrypto(getApplicationContext());
        try {
            byte[] encrypted= crypto.encryptWithSalt(dat.getBytes(), salt.getBytes());
            MyLogger.debug("encrypted " + new String(encrypted));
            byte[] decrypted= crypto.decryptWithSalt(encrypted, salt.getBytes());
            MyLogger.debug("Decrypted "+new String(decrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (hasVaultOperations) {
            vaultASyncTask = new VaultAsyncTask(getApplicationContext());
            MyLogger.debug("onPause has onEncrypt AddLockedVideoFragment.");
            if (Statics.buildType==BUILD_TYPES.RELEASE){
                userHelper.logoutUser();
                vaultASyncTask.onEncrypt();
            }
        } else {
            MyLogger.debug("onPause Has no Vault Operation in AddLockedVideoFragment.");
        }
        // Timer will still run and after the time elapsed it will show dialog to envrypt the vault.rr
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        int fragmentStackBefore = fragmentStack.size();
        MyLogger.debug("fragmentStack count before " + fragmentStackBefore);
        if (fragmentStackBefore==1) {
            finish();
        }
       if (fragmentStackBefore>1) {
            BaseFragment stackPoppedFragment= (BaseFragment) fragmentStack.pop();
            int fragmentStackAfter = fragmentStack.size();
            MyLogger.debug("fragmentStack count after " + fragmentStackAfter);
            stackPoppedFragment.backPressedInFragment();
            getSupportFragmentManager().beginTransaction().remove(stackPoppedFragment).commit();
            if(fragmentStackAfter>=1){
                MyLogger.debug("Peeking the top fragment3 onBackPressed ");
                BaseFragment stackPeekFragment= (BaseFragment) fragmentStack.elementAt(fragmentStackAfter-1);
                supportFragmentManager.beginTransaction().
                        replace(R.id.main_content, stackPeekFragment).commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasVaultOperations) {
            vaultASyncTask = new VaultAsyncTask(getApplicationContext());
            MyLogger.debug("onResume has onDecrypt AddLockedVideoFragment.");
            vaultASyncTask.onDecrypt();
        } else {
            MyLogger.debug("onResume Has no Vault Operation in AddLockedVideoFragment.");
        }
        userHelper.sendToLoginIfNotLoggedIn(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                userHelper.logoutUser();
                finish();
                startLogInActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(OnCheckDrawerPosition event) {
        navigationView.getMenu().getItem(event.getPosition()).setChecked(true);
    }

}

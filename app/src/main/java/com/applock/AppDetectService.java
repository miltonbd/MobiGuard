package com.applock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;

import com.fs.lib.util.MyLogger;
import com.fs.lib.util.SystemHelper;
import com.util.MyDatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * starting the app from service will list all videos and save the in db.
 * later when adding new video from db. service will add the videos when available.
 * It will check when an apps go foreground to lock the app again.
 */
public class AppDetectService extends Service {
    final Handler mHandler = new Handler();
    private int CHECKING_INTERVAL=1000;
    private MyDatabaseHelper db;
    private List<LockedApps> lockedApps = new ArrayList<LockedApps>();
    private List<LockedApps> unLockedApps = new ArrayList<LockedApps>(); // Apps unlocked by AppUnLockActivity
    public boolean hasUnLockApp =false;
    private LockedApps foregroundApp;


    public void onEvent( OnHasUnLockedApps event) {
        MyLogger.debug("OnHasUnLockedApps event received");
        this.hasUnLockApp =event.isHasUnLockedApp();
        this.foregroundApp=event.getUnlockedApp();
        MyLogger.debug("Unlock app received in service "+event.getUnlockedApp());

    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                lockedApps=  db.getLockedApps();
                unLockedApps=  db.getUnLockedApps();
               /* MyLogger.debug("Total Locked Apps "+lockedApps.size());
                MyLogger.debug("Total UnLocked Apps " + unLockedApps.size());*/
            } catch (SQLException e) {
                MyLogger.debug("lock and unlock apps listing exception "+e.getMessage());
                e.printStackTrace();
            }
            String[] activeForegroundPackages= SystemHelper.getForegroundApps(getApplicationContext());
         //   MyLogger.debug("Foreground Activity Count "+activeForegroundPackages.length);
            if (activeForegroundPackages != null) {
                for (String  foregroundPackage: activeForegroundPackages) {
                    /**
                     * Foreground package is not unLocked Package Means the unlock app goes Background
                     * hence, lock the app again
                     */
                    if (hasUnLockApp&&!foregroundPackage.matches("com.fs.mobiguard")) {
                        MyLogger.debug(foregroundPackage+"="+foregroundApp.getPackageName());
                        if (foregroundApp.getPackageName().matches(foregroundPackage)){
                            MyLogger.debug("App is still unlocked. ");
                        }else{
                            try {
                                db.lockApp(foregroundApp.getPackageName());
                                hasUnLockApp=false;
                                foregroundApp=null;
                                MyLogger.debug("App is locked Again.");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    for ( LockedApps lockedApp:lockedApps) {
                      // locking stuffs
                        if (foregroundPackage.matches(lockedApp.getPackageName())) {
                           ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                            am.killBackgroundProcesses(lockedApp.getPackageName());
                            MyLogger.debug(lockedApp.getPackageName() + "=" + foregroundPackage);
                            Intent i = new Intent(getApplicationContext(), AppUnLockActivity.class);
                            // set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("package", lockedApp.getPackageName());
                            i.putExtra("appName",lockedApp.getAppName());
                            i.putExtra("id",lockedApp.getId());
                            startActivity(i);
                        }
                    }
                }
            }

            mHandler.postDelayed(this, CHECKING_INTERVAL);
        }
       public  void startNewMainActivity( Class<? extends Activity> newTopActivityClass) {

        }


    };

    @Override
    public void onCreate() {
        super.onCreate();
        MyLogger.debug("onCreate");
        db =MyDatabaseHelper.getInstance(getApplicationContext());
        EventBus.getDefault().register(this);

    }
    

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLogger.debug("Service Started");

        mHandler.postDelayed(runnable, 1000);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        MyLogger.debug("Service Destroy");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
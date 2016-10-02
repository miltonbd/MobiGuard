package com.applock;

import android.app.ActivityManager;

/**
 * Created by milton on 30/12/15.
 */
public class AppLockHelper {
    public static void startTimer (){
/*        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                List<ActivityManager.RunningAppProcessInfo> appProcesses= activityManager.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    try {
                        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            if (!lastFrontAppPkg.equals((String) appProcess.pkgList[0])) {
                                apkInfo = ApkInfo.getInfoFromPackageName(appProcess.pkgList[0], mContext);
                                if (apkInfo == null || (apkInfo.getP().applicationInfo.flags && ApplicationInfo.FLAG_SYSTEM) == 1) {
                                    // System app                                             continue;
                                } else if (((apkInfo.getP().versionName == null)) || (apkInfo.getP().requestedPermissions == null)) {
                                    //Application that comes preloaded with the device
                                    continue;
                                } else {
                                    lastFrontAppPkg = (String) appProcess.pkgList[0];
                                }
                                //kill the app
                                //Here do the pupop with password to launch the lastFrontAppPkg if the pass is correct
                            }
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
    }, 0, 1000);*/

    }
    ActivityManager am;
    public AppLockHelper(ActivityManager am ) {
        this.am = am;
    }
    public int findPIDbyPackageName(String packagename) {
        int result = -1;

        if (am != null) {
            for (ActivityManager.RunningAppProcessInfo pi : am.getRunningAppProcesses()) {
                if (pi.processName.equalsIgnoreCase(packagename)) {
                    result = pi.pid;
                }
                if (result != -1) break;
            }
        } else {
            result = -1;
        }

        return result;
    }

    public boolean isPackageRunning(String packagename) {
        return findPIDbyPackageName(packagename) != -1;
    }

    public boolean killPackageProcesses(String packagename) {
        boolean result = false;

        if (am != null) {
            am.killBackgroundProcesses(packagename);
            result = !isPackageRunning(packagename);
        } else {
            result = false;
        }

        return result;
    }
}

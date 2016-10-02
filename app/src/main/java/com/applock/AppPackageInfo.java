package com.applock;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AppPackageInfo {

    private final MyDatabaseHelper db;
    private Context context;

    public AppPackageInfo(Context context) {
        this.context = context;
        db = MyDatabaseHelper.getInstance(context);
    }

    public ArrayList<AppInfoPojo> getPackages(boolean systemAppInclude) {
        ArrayList<AppInfoPojo> apps = getInstalledApps(false, systemAppInclude); /* false = no system packages */
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i);
        }
        return apps;
    }

    private boolean isSystemPackage(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private ArrayList<AppInfoPojo> getInstalledApps(boolean getSysPackages, boolean systemAppInclude) {
        ArrayList<AppInfoPojo> res = new ArrayList<AppInfoPojo>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

            if (systemAppInclude) {
                if ((!getSysPackages) && (p.versionName == null)) {
                    continue;
                }
            } else {
                if (isSystemPackage(p)) { // test if package is found in LockedApps
                    continue;
                }

            }
            if(db.isPackageAlreadyLocked(p.packageName)){
                continue;
            }
            AppInfoPojo newLockedApp = new AppInfoPojo();
            newLockedApp.setAppName(p.applicationInfo.loadLabel(context.getPackageManager()).toString());
            newLockedApp.setPackageName(p.packageName);
            newLockedApp.setVersionName(p.versionName);
            newLockedApp.setVersionCode(p.versionCode);
            newLockedApp.setIconDrawable(p.applicationInfo.loadIcon(context.getPackageManager()));
            newLockedApp.setIcon(p.applicationInfo.icon);

            if (!p.packageName.matches("com.fs.mobiguard")) {
                res.add(newLockedApp);
            }
        }
        return res;
    }
}
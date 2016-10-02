package com.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import com.user.LoginActivity;

/**
 * IMPORTANT: It's somehow tricky if you need to do something with main activity in your app when it's hidden. you will face an ActivityNotFoundException. to make it work, you should unhide icon before doing anything to your main activity and hide it again after you are finished.
 simple steps:
 1-call received here
 2-unhide icon
 3-launch main activity
 4-do your things on main activity
 5-hide icon again
 */
public class ApplicationHelper {
    public static void hideAppIcon(Context context) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, LoginActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
    public static void showAppIcon(Context context) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, LoginActivity.class);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}

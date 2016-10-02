package com.applock;

/**
 * Created by milton on 12/02/16.
 */
public class OnHasUnLockedApps {
    private boolean hasUnLockedApp;
    private LockedApps unlockedApp;

    public OnHasUnLockedApps(boolean hasUnLockedApp, LockedApps unlockedApp) {
        this.hasUnLockedApp = hasUnLockedApp;
        this.unlockedApp = unlockedApp;
    }

    public boolean isHasUnLockedApp() {
        return hasUnLockedApp;
    }

    public void setHasUnLockedApp(boolean hasUnLockedApp) {
        this.hasUnLockedApp = hasUnLockedApp;
    }

    public LockedApps getUnlockedApp() {
        return unlockedApp;
    }

    public void setUnlockedApp(LockedApps unlockedApp) {
        this.unlockedApp = unlockedApp;
    }
}

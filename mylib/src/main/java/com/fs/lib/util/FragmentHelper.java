package com.fs.lib.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by milton on 11/02/16.
 */
public class FragmentHelper {

    public static int getTotalFragments(FragmentManager fragmentManager){
        return fragmentManager.getBackStackEntryCount();
    }

    public static void removeTopFragment(FragmentManager fragmentManager) {
        Fragment topFragment=getTopFragment(fragmentManager);
        if (topFragment!=null) {
            MyLogger.debug("Removing the Fragmnet");
            fragmentManager.beginTransaction().remove(topFragment).commit();
            fragmentManager.popBackStackImmediate();
        }

    }

    public static Fragment getTopFragment(FragmentManager fragmentManager) {
        List<Fragment> fragentList = fragmentManager.getFragments();
        Fragment top = null;
        for (int i = fragentList.size() -1; i>=0 ; i--) {
            top = (Fragment) fragentList.get(i);
            if (top != null) {
                return top;
            }
        }
        return top;
    }

    public static void removeFragment(FragmentManager fg, Fragment fragment) {
        fg.beginTransaction().remove(fragment).commit();
    }
}

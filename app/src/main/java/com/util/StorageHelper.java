package com.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by milton on 9/01/16.
 */
public class StorageHelper {
    public static void printStorage() {
        File base= Environment.getExternalStorageDirectory();
        com.fs.lib.util.MyLogger.debug("base " + base.getAbsolutePath());
    }
}

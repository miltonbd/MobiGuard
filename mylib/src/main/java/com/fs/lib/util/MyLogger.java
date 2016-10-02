package com.fs.lib.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by milton on 19/12/15.
 */
public class MyLogger {
    private static String TAG="MobiGuard";
    public static void debug(String msg)
    {
        Log.d(TAG,msg);
    }
    public static void debug(int msg)
    {
        Log.d(TAG,String.valueOf(msg));
    }
    public static void show(Context context, String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void debugPath(String msg, String picturePath) {
        File file=new File(picturePath);
        if (file.exists()) {
            msg+=" Exists with size "+file.length();
            Log.d(TAG,msg);
        }else {
            Log.d(TAG,msg+" file does not exists");
        }
    }

    public static void debugArray(String s, ArrayList<String> allImages) {
        Log.d(TAG,s);
        Log.d(TAG,"size is "+allImages.size());
        for (String img:allImages){
            Log.d(TAG,img);
        }
    }
}

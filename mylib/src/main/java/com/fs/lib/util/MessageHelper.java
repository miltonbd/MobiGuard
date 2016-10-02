package com.fs.lib.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by milton on 21/02/16.
 */
public class MessageHelper {
    public static void showInfo(AppCompatActivity activity,String msg){
        Crouton.showText(activity,msg, Style.INFO);
    }
    public static void showInfo(AppCompatActivity activity,int msg){
        Crouton.showText(activity,msg, Style.INFO);
    }
    public static void showAlert(AppCompatActivity activity,String msg){
        Crouton.showText(activity,msg, Style.ALERT);
    }
    public static void showAlert(AppCompatActivity activity,int msg){
        Crouton.showText(activity,msg, Style.INFO);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG);
    }
}

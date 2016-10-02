package com.fs.lib.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by milton on 17/02/16.
 */
 public abstract class BaseAlertDialog extends  AlertDialog.Builder implements DialogInterface.OnClickListener {
    private int titleResource;
    private CharSequence[] options;

    public BaseAlertDialog(Context context, int titleResource,CharSequence[] options) {
        super(context);
        this.titleResource = titleResource;
        this.options = options;
    }
    public void showDialog(){
        setTitle(getContext().getResources().getString(titleResource));
        setItems(options,this);
        show();
    }
}


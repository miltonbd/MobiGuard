package com.fs.lib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


/**
 * Created by milton on 21/01/16.
 */
public abstract class OnConfirmDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener {

    public OnConfirmDialog(Context context,String message) {
        super(context);
        setMessage(message).setCancelable(false);
        setPositiveButton("Yes",this);
        setNegativeButton("No",this);
    }

    @Override
    public AlertDialog.Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        return super.setOnCancelListener(onCancelListener);
    }
}

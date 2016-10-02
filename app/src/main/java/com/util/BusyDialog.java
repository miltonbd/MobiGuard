package com.util;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.mobiguard.MyApp;

/**
 * Created by milton on 5/02/16.
 */
public abstract class BusyDialog extends AsyncTask<Void,Void,Void> {
    public OnExecuteCallback callback;
    private ProgressDialog pg;
    public String p;

    public BusyDialog(String p){
        this.p = p;
    }

    public void setCallback(OnExecuteCallback callback) {
        this.callback = callback;
    }

    public String getP() {
        return p;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pg=new ProgressDialog(MyApp.getMainActivity());
        pg.setMessage("Please wait....");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pg.dismiss();
    }
    public static interface OnExecuteCallback{
        public void onExecute();
    }
}

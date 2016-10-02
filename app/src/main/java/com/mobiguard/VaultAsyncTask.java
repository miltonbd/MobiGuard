package com.mobiguard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.crypto.Crypto;
import com.crypto.CryptoHelper;

/**
 * Created by milton on 11/01/16.
 */
public class VaultAsyncTask extends AsyncTask<Void,Void,Void> {
    private final CryptoHelper cryptoHelper;
    private Crypto crypto;
    private boolean isEncrypt=true;
    private ProgressDialog pg;

    public VaultAsyncTask(Context context) {
        this.crypto = Crypto.getCrypto(context);
        this.cryptoHelper=CryptoHelper.getInstannce(context);
    }

    public void setEncrypt(boolean isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public boolean getEncrypt() {
        return this.isEncrypt ;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*pg = new ProgressDialog(MyApp.getMainActivity());
        pg.setMessage("Loading");
        pg.show();*/
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(isEncrypt) {
            cryptoHelper.encrypVault(crypto);
        } else {
            cryptoHelper.decryptVault(crypto);
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //pg.dismiss();
    }

    public void onEncrypt() {
        setEncrypt(true);
        execute();
    }

    public void onDecrypt() {
        setEncrypt(false);
        execute();
    }
}

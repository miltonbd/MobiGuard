package com.mobiguard;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.fs.lib.util.MyLogger;
import com.fs.lib.util.VideoFileFinder;
import com.util.MyDatabaseHelper;
import com.util.Statics;
import com.util.StorageHelper;
import com.videos.LockedVideoGifCreator;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by milton on 30/12/15.
 */
public class MyApp extends Application {
    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MyApp.mainActivity = mainActivity;
    }

    private static MainActivity mainActivity;
    public static Context getContext() {
        return context;
    }

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        StorageHelper.printStorage();
        MyLogger.debug("Application created.");
        context = getApplicationContext();

        //startService(new Intent(this, AppDetectService.class));
        File appFolderfile = new File(Statics.appFolder);
        if (!appFolderfile.exists()) {
            appFolderfile.mkdirs();
        }
        File vaultFolder = new File(Statics.vaultFolder);
        if (!vaultFolder.exists()) {
            vaultFolder.mkdirs();
        }

        File vaultIconFolder = new File(Statics.vaultIconFolder);
        if (!vaultIconFolder.exists()) {
            vaultIconFolder.mkdirs();
        }
        File vaultGifIconFolder = new File(Statics.vaultGifIconFolder);
        if (!vaultGifIconFolder.exists()) {
            vaultGifIconFolder.mkdirs();
        }

        File vaultAudioFolder = new File(Statics.vaultAudioFolder);
        if (!vaultAudioFolder.exists()) {
            vaultAudioFolder.mkdirs();
        }

        File vaultVideoFolder = new File(Statics.vaultVideoFolder);
        if (!vaultVideoFolder.exists()) {
            vaultVideoFolder.mkdirs();
        }

        File vaultCallRecordFolder = new File(Statics.vaultCallRecordFolder);
        if (!vaultCallRecordFolder.exists()) {
            vaultCallRecordFolder.mkdirs();
        }

        try {
            startVideoGifMaker();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        MyLogger.debug("Country code "+countryCodeValue);
    }

        public static void startVideoGifMaker() throws SQLException {
            VideoFileFinder videoFileFinder=new VideoFileFinder();
            MyDatabaseHelper db=MyDatabaseHelper.getInstance(getContext());
            // when the videos could is same as
            for(String fileString:videoFileFinder.getFileList()){
                // test if actual path already exists in LockedVideoFiles table
                if (!db.deleteVideoIfNotExists(fileString)) {
                    LockedVideoGifCreator lockedVideoGifCreator=new LockedVideoGifCreator(getContext(),fileString);
                    MyLogger.debug("Staring new thread of "+lockedVideoGifCreator.getClass().getName());
                    lockedVideoGifCreator.execute();
                }
            }
        }



/*
        String s=Environment.getExternalStorageDirectory().getAbsolutePath();
        String inputFilePath = s + "/DCIM/Camera/1.3gp";

        VideoFileFinder ff=new VideoFileFinder();
        ArrayList<String> files=ff.getFileList();*/


    /*    test();
        try {
            test2();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/


public void startGifrFromVideo(){
         /*   for (String f:files){
            LockedVideoGifCreator l=new LockedVideoGifCreator(getContext(),f);
            l.execute();
        }*/
}

    private void test2() throws NoSuchAlgorithmException {
/*        String key="milton";
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(key.getBytes());
        String path="/storage/sdcard0/DCIM/Camera/1.jpg";
        try {
            Crypto cr=new Crypto(thedigest);
            byte[] input= cr.read(new File(path));
            byte[] putEnc= cr.encrypt(input, thedigest);
            byte[] out= cr.decrypt(putEnc, thedigest);
            Crypto.write("/storage/sdcard0/DCIM/Camera/11.jpg", out);
            MyLogger.debug("succ");
        } catch (IOException e) {
            MyLogger.debug(e.getMessage());
        }*/
    }

    public void test () {
        File f = new File(Statics.vaultFolder);
        File[] files= f.listFiles();
        MyLogger.debug("Total file in .vault "+files.length);
        for ( File g:files) {
            MyLogger.debug("file "+g.getPath());
        }
        String key = "1234567891234567";
        String data = "example";
       // MyLogger.debug(" output "+new String(Crypto.decrypt(Crypto.encrypt(data.getBytes(), key), key)));
    }
}



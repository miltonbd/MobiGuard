package com.util;

import android.os.Environment;
import java.util.Arrays;
import java.util.List;

/**
 * Created by milton on 19/12/15.
 */
public class Statics {
    public static final int VIDEO_DURATION_THRESHOLD=10000; // IN MICRO SECONDS
    public static String  extPath= Environment.getExternalStorageDirectory().toString()+"/";
    public static String  appFolder= Environment.getExternalStorageDirectory().toString()+"/MobiGuard/";
    public static String  vaultFolder= Environment.getExternalStorageDirectory().toString()+"/MobiGuard/.vault/";
    public static String vaultZipFile = Environment.getExternalStorageDirectory().toString()+"/MobiGuard/.vault.zip";
    public static String  vaultIconFolder= Environment.getExternalStorageDirectory().toString()+"/MobiGuard/.vault/icons/";
    public static String  vaultGifIconFolder= Environment.getExternalStorageDirectory().toString()+"/MobiGuard/.vault/icons/gif/";
    public static String  vaultAudioFolder= Environment.getExternalStorageDirectory().toString()+"/MobiGuard/.vault/audio/";
    public static String  vaultVideoFolder= Environment.getExternalStorageDirectory().toString()+"/MobiGuard/.vault/video/";
    public static String  vaultCallRecordFolder= Environment.getExternalStorageDirectory().toString()+"/MobiGuard/.vault/call_record/";
    public static String vault=".vault";
    public static String ComputerLogin= "Computer Login";
    public static List<String> fieldsComputerLogin= Arrays.asList("Account","Name","Password","Hint","note");
    public static String CreditCard= "Credit Card";
    public static List<String> CreditCardFields= Arrays.asList("Card Name","Card Number","Pin","Holder","Expiration","Security Code",
            "Payment Limit \n Internet","Payment Limit Shop","Note");
    public static String OnlineBank= "Online Bank";
    public static List<String> OnlineBankFields= Arrays.asList("Bank","Web Site","Name","Password","Note");
    public static String Ecommerce= "Ecommerce";
    public static List<String> EcommerceFields= Arrays.asList("Website","Name","Password","Note");
    public static String Email="Email";
    public static List<String> EmailFields= Arrays.asList("Account","Email","Password","Web Site","Note");
    public static List<String> WebAccountFields= Arrays.asList("Web Site","Name","Password","Note");
    public static String WebAccount= "Web Accounts";

    public static String FRAGMENT_TAG_DASHBOARD= "DASHBOARD";
    public static String FRAGMENT_TAG_PASSWORD= "PASSWORD";
    public static String FRAGMENT_TAG_ADD_PASSWORD= "ADD_PASSWORD";
    public static String FRAGMENT_TAG_APPLOCK= "APPLOCK";
    public static String FRAGMENT_TAG_SETTING= "SETTING";
    public static String FRAGMENT_TAG_PICTURE= "PICTURE";

    public static String FRAGMENT_TAG_VIDEO= "VIDEO";
    public static String FRAGMENT_TAG_PASSWORD_CONTAINER="passwordsContainerFragment";

    public static BUILD_TYPES buildType=BUILD_TYPES.RELEASE;

}

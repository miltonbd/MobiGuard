package com.crypto;

import android.content.Context;

import com.fs.lib.util.FileHelper;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.ZipUtils;
import com.mobiguard.MyApp;
import com.util.MyDatabaseHelper;
import com.util.Statics;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by milton on 12/01/16.
 */
public class CryptoHelper {
      /*  Encrypt the fields when doing any Add/Edit Operations of an object
       */
    /**
     * Only type String is allowed
     *
     * @param classInstance
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     *
     */
    private static CryptoHelper instance;
    private Context context;
    private static Crypto crypto;

    private CryptoHelper(Context context) {
        this.context = context;
    }

    public static CryptoHelper getInstannce(Context context){
        if (instance==null) {
            instance=new CryptoHelper(context);
            crypto = Crypto.getCrypto(context);
        }
        return instance;
    }

    public static CryptoHelper getInstannce(){
        Context context= MyApp.getContext();
        if (instance==null) {
            instance=new CryptoHelper(context);
            crypto = Crypto.getCrypto(context);
        }
        return instance;
    }

    public  void decryptFields( final Object classInstance) {
        // Get the private field
        for (Field declaredField : classInstance.getClass().getDeclaredFields()) {
            // Sets the field to the new value for this instance
            if (!isInNotEncypt(declaredField.getName())) {
                declaredField.setAccessible(true);
                String value= null;
                try {
                    if( declaredField.getType().isAssignableFrom(String.class) ){

                        value = String.valueOf(declaredField.get(classInstance));
                        String encryptedValue= new String(crypto.decrypt(value.getBytes()));
                        MyLogger.debug("Field value " + value);
                        MyLogger.debug("Field value Encrypted " + encryptedValue);
                        //  MyLogger.debug("Field value Decrypted " + new String(crypto.decrypt(encryptedValue.getBytes())));

                        declaredField.set(classInstance, encryptedValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }
      /*  Encrypt the fields when doing any Add/Edit Operations of an object
     *
     * @param classInstance
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public  void encryptFields( final Object classInstance) {;
        // Get the private field
        for (Field declaredField : classInstance.getClass().getDeclaredFields()) {
            // Sets the field to the new value for this instance
            if (!isInNotEncypt(declaredField.getName())) {
                declaredField.setAccessible(true);
                String value= null;
                try {
                    if( declaredField.getType().isAssignableFrom(String.class) ){

                        value = String.valueOf(declaredField.get(classInstance));
                        String encryptedValue= new String(crypto.encrypt(value.getBytes()));
                        MyLogger.debug("Field value " + value);
                        MyLogger.debug("Field value Encrypted " + encryptedValue);
                        //  MyLogger.debug("Field value Decrypted " + new String(crypto.decrypt(encryptedValue.getBytes())));

                        declaredField.set(classInstance, encryptedValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public  boolean isInNotEncypt(Object field) {
        HashMap<String, Boolean> encryptionToEscapeFields = getAllNotEncryptFields();
        return encryptionToEscapeFields.containsKey(field);
    }

    public  HashMap<String, Boolean> getAllNotEncryptFields() {
        HashMap<String, Boolean> encryptionToEscapeFields = new HashMap<>();
        encryptionToEscapeFields.put(MyDatabaseHelper.id, true);
        encryptionToEscapeFields.put(MyDatabaseHelper.order, true);
        encryptionToEscapeFields.put(MyDatabaseHelper.icon, true);
        encryptionToEscapeFields.put(MyDatabaseHelper.isLocked, true);

        return encryptionToEscapeFields;
    }
    public  boolean isFilesPresentInVault() {
        File f = new File(Statics.vaultFolder);
        File[] files= f.listFiles();
        if(files.length>1) {
            return  true;
        }
        return false;
    }

    public  void encrypVault(Crypto crypto)
    {   if (isFilesPresentInVault()) {
        try {
            // zip the vault then encrypt
            ZipUtils.zipVault(Statics.vaultFolder, Statics.vaultZipFile, true);
            byte[] input = FileHelper.read(new File(Statics.vaultZipFile));
            byte[] putEnc= crypto.encrypt(input);
            FileHelper.write(Statics.vaultZipFile, putEnc);
        } catch (IOException e) {
            e.printStackTrace();
            MyLogger.debug(e.getMessage());
        }
    }

    }
    public  void decryptVault(Crypto crypto){
        try {
            // if zip file is present then decrypt and unzip it.
            if(FileHelper.isFileExists(Statics.vaultZipFile)) {
                byte[] input = FileHelper.read(new File(Statics.vaultZipFile));
                byte[] putEnc= crypto.decrypt(input);
                FileHelper.write(Statics.vaultZipFile, putEnc);
                //ZipUtils.unpackZip(Statics.vaultZipFile);
                //FileHelper.deleteFile(Statics.vaultZipFile);
                ZipUtils.unpackZip(new File(Statics.vaultZipFile), new File(Statics.appFolder));
            }
        } catch (IOException e) {
            e.printStackTrace();
            MyLogger.debug(e.getMessage());
        }
    }

}

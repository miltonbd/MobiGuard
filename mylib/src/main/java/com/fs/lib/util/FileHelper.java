package com.fs.lib.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by milton on 30/12/15.
 * This will help in files of any types.
 */
public class FileHelper {
   public static void moveFile(String inputPath, String outputPath) {
       // create output file if it doe not exists

       try {
           InputStream in = null;
           OutputStream out = null;

           File outputFile = new File(outputPath);
           if (!outputFile.exists()) {
               outputFile.createNewFile();
           }

           in = new FileInputStream(inputPath);
           out = new FileOutputStream(outputPath);

           byte[] buffer = new byte[1024];
           int read;
           while ((read = in.read(buffer)) != -1) {
               out.write(buffer, 0, read);
           }
           in.close();
           in = null;

           // write the output file
           out.flush();
           out.close();
           out = null;
           // delete the input path
           File deleteFile = new File(inputPath);
           if (deleteFile.exists()) {
               deleteFile.delete();
           }
       } catch (FileNotFoundException e) {
           MyLogger.debug("Moving file exception" + e.getMessage());
       } catch (Exception e) {
           MyLogger.debug("Moving file exception" + e.getMessage());
       }
   }

       public static List<String> getStorages(){
           List<String> storageList= new ArrayList<>();
           final String state = Environment.getExternalStorageState();

           if ( Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {  // we can read the External Storage...
               //Retrieve the primary External Storage:
               final File primaryExternalStorage = Environment.getExternalStorageDirectory();

               //Retrieve the External Storages root directory:
               final String externalStorageRootDir;
               if ( (externalStorageRootDir = primaryExternalStorage.getParent()) == null ) {  // no parent...
                   MyLogger.debug("External Storage: " + primaryExternalStorage + "\n");
                   storageList.add(primaryExternalStorage.getAbsolutePath());
               }
               else {
                   final File externalStorageRoot = new File( externalStorageRootDir );
                   final File[] files = externalStorageRoot.listFiles();

                   for ( final File file : files ) {
                       if ( file.isDirectory() && file.canRead() && (file.listFiles().length > 0) ) {  // it is a real directory (not a USB drive)...
                           MyLogger.debug("External Storage: " + file.getAbsolutePath() + "\n");
                           storageList.add(file.getAbsolutePath());
                       }
                   }
               }}
           return storageList;
       }


    public static  String getPathFromUri(Uri uri, Context context) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getFileNameFromPath(String path) {
        File f= new File(path);
        return f.getName();
    }

    public static boolean isFileExists(String path) {
        File f= new File(path);
        return f.isFile()&&f.exists();
    }

    public static boolean isDirExists(String path) {
        File f= new File(path);
        return f.isDirectory()&&f.exists();
    }


    public static void deleteDirectory(String deletePath) {
        File fileOrDirectory= new File(deletePath);
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteDirectory(child.getAbsolutePath());

            fileOrDirectory.delete();
        }
    public static void createDirectory(String dirPath) {
        File f=new File(dirPath);
        if  (!f.exists()) {
            f.mkdirs();
        }
    }


    public static void write(String path, byte[] myByteArray) throws IOException {
	/*	File file = new File(path);
		if (file.exists()) {
			file.delete();
		}*/
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(myByteArray);
        fos.close();
    }

    public static byte[] read(File file) throws IOException {

        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException(
                        "EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return buffer;
    }

    public static void deleteFile(String path)  {
        File f= new File(path);
        if(f.exists()) f.delete();
    }
    public static boolean copyFile(String inputPath, String outputPath) {
        boolean returnValue=true;
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File file = new File (outputPath);
            File dir= new File(file.getParent());
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            if (!file.exists())
            {
                file.createNewFile();
            }

            in = new FileInputStream(inputPath );
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException e) {
            MyLogger.debug("Coping file " + e.getMessage());
            returnValue=false;

        }
        catch (Exception e) {
            MyLogger.debug("Coping file " + e.getMessage());
            returnValue=false;
        }
        return returnValue;
    }
}

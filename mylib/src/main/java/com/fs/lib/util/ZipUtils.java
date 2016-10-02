package com.fs.lib.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by milton on 10/01/16.
 */
public class ZipUtils {
    /*
 *
 * Zips a file at a location and places the resulting zip file at the toLocation
 * Example: zipVault("downloads/myfolder", "downloads/myFolder.zip");
 */

    public static boolean zipVault(String sourcePath, String toLocation, boolean deleteInput) {
        // ArrayList<String> contentList = new ArrayList<String>();
        final int BUFFER = 2048;
        File outputFile = new File(toLocation);
    /*    if(outputFile.exists()) {
            outputFile.delete();
        }*/
        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if(deleteInput) {
            FileHelper.deleteDirectory(sourcePath);
        }
        return true;
    }

/*
 *
 * Zips a subfolder
 *
 */

    private static void zipSubFolder(ZipOutputStream out, File folder,
                              int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                Log.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    /*
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */
    private static String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    private static void  createDir(File dir) {
        if (dir.exists()) {
            return;
        }
        //Log.AddPasswordFragment(TAG, "Creating dir " + dir.getName());
        if (!dir.mkdirs()) {
            throw new RuntimeException("Can not create dir " + dir);
        }
    }

    static public void unpackZip(File source, File destination) throws IOException
    {

            System.out.println("Unzipping - " + source.getName());
            int BUFFER = 2048;

            ZipFile zip = new ZipFile(source);
            try{
                destination.getParentFile().mkdirs();
                Enumeration zipFileEntries = zip.entries();

                // Process each entry
                while (zipFileEntries.hasMoreElements())
                {
                    // grab a zip file entry
                    ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                    String currentEntry = entry.getName();
                    File destFile = new File(destination, currentEntry);
                    //destFile = new File(newPath, destFile.getName());
                    File destinationParent = destFile.getParentFile();

                    // create the parent directory structure if needed
                    destinationParent.mkdirs();

                    if (!entry.isDirectory())
                    {
                        BufferedInputStream is = null;
                        FileOutputStream fos = null;
                        BufferedOutputStream dest = null;
                        try{
                            is = new BufferedInputStream(zip.getInputStream(entry));
                            int currentByte;
                            // establish buffer for writing file
                            byte data[] = new byte[BUFFER];

                            // write the current file to disk
                            fos = new FileOutputStream(destFile);
                            dest = new BufferedOutputStream(fos, BUFFER);

                            // read and write until last byte is encountered
                            while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                                dest.write(data, 0, currentByte);
                            }
                        } catch (Exception e){
                            System.out.println("unable to extract entry:" + entry.getName());
                            throw e;
                        } finally{
                            if (dest != null){
                                dest.close();
                            }
                            if (fos != null){
                                fos.close();
                            }
                            if (is != null){
                                is.close();
                            }
                        }
                    }else{
                        //Create directory
                        destFile.mkdirs();
                    }

                    if (currentEntry.endsWith(".zip"))
                    {
                        // found a zip file, try to extract
                        unpackZip(destFile, destinationParent);
                        if(!destFile.delete()){
                            System.out.println("Could not delete zip");
                        }
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Failed to successfully unzip:" + source.getName());
            } finally {
                zip.close();
            }
            System.out.println("Done Unzipping:" + source.getName());
        source.delete();
        }
}

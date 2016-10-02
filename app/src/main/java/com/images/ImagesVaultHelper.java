package com.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import com.fs.lib.util.FileHelper;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.MyMediaMetaDataRetriever;
import com.fs.lib.util.ViewHelper;
import com.mobiguard.MyApp;
import com.fs.lib.util.ImageHelper;
import com.util.MyDatabaseHelper;
import com.util.Statics;
import com.videos.LockedVideoFiles;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import static com.util.Statics.vaultGifIconFolder;
import static com.util.Statics.vaultIconFolder;

/**
 * Created by milton on 11/01/16.
 */
public class ImagesVaultHelper {

    /**
     * creates scalled image from input imagePath
     *
     * @param imagePath
     */
    public static boolean generateIcon(String imagePath, int width, int height) {
        float heightPixel = ViewHelper.convertDpToPixel(height, MyApp.getContext());
        float widthPixel = ViewHelper.convertDpToPixel(width, MyApp.getContext());
        return ImageHelper.lessResolution(imagePath, getIconPath(imagePath), widthPixel, heightPixel);
    }

    public static String getIconPath(String fileName) {
        return vaultIconFolder + "/ic_" + FileHelper.getFileNameFromPath(fileName);
    }

    public static void generateIconVideo(Context context, String outputFilePath) {
        MediaMetadataRetriever mmRetriever = new MediaMetadataRetriever();
        mmRetriever.setDataSource(outputFilePath);

        String time = mmRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInmillisec = Long.parseLong(time);
        long duration = timeInmillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        long seconds = duration - (hours * 3600 + minutes * 60);

        String s = Environment.getExternalStorageDirectory().getAbsolutePath();
        Uri myVideoUri = Uri.parse(outputFilePath);
        MediaPlayer mp = MediaPlayer.create(context, myVideoUri);
        // we need 10 frames to make gif
        int N = (int) Math.floor(seconds / 3);
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 1; i < seconds; i += N) {
            bitmaps.add(mmRetriever.getFrameAtTime(i * 100000));
        }

        byte[] bytes = ImageHelper.generateGIF(bitmaps);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/test.gif");
            outStream.write(bytes);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public static void makeVideoThumbnail(Context context, String iconPath, String inputFilePath) {
        MyLogger.debug(" Making Video Gif File makeVideoThumbnail");
        MyMediaMetaDataRetriever
                mmRetriever =new MyMediaMetaDataRetriever();
        try {
            MyDatabaseHelper db=MyDatabaseHelper.getInstance(context);
            LockedVideoFiles lockedVideoFiles=new LockedVideoFiles();
            lockedVideoFiles.setIconPath(iconPath);
            lockedVideoFiles.setActualPath(inputFilePath);
            lockedVideoFiles.setIsInvalid(true);
            Dao<LockedVideoFiles, Long> dao= db.getLockedVideosFilesDao();
            FileInputStream inputStream = new FileInputStream(inputFilePath);
            Uri myVideoUri = Uri.parse(inputFilePath);
            mmRetriever.setDataSource(context, myVideoUri);
            inputStream.close();
            MediaPlayer mp = MediaPlayer.create(context, myVideoUri);
            if (mp.getDuration()< Statics.VIDEO_DURATION_THRESHOLD) {
                MyLogger.debug("Video Length is less than 30 seconds");
                dao.create(lockedVideoFiles);
                return;
            }
            lockedVideoFiles.setIsInvalid(false);
            Bitmap bitmap1 = mmRetriever.getFrameAtTime(1000000);
            Bitmap bitmap2 = mmRetriever.getFrameAtTime(2000000);
           // Bitmap bitmap3 = mmRetriever.getFrameAtTime(3000000);
            List<Bitmap> bitmaps = Arrays.asList(bitmap1, bitmap2
            );

            MyLogger.debug("Video Length "+mp.getDuration()/1000+" Seconds");
            byte[] bytes = ImageHelper.generateGIF(bitmaps);
            FileOutputStream outStream = null;
            File iconFile=new File(iconPath);
            if (!iconFile.exists()) {
                iconFile.createNewFile();
                dao.create(lockedVideoFiles);
                MyLogger.debug("makeVideoThumbnail creating new icon file");

            }
            outStream = new FileOutputStream(iconFile);
            outStream.write(bytes);
            outStream.close();
            MyLogger.debug("makeVideoThumbnail finished without Exception");

        } catch (FileNotFoundException e) {
            MyLogger.debug("FileNotFoundException in makeVideoThumbnail "+e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            MyLogger.debug("IOException in makeVideoThumbnail " + e.getMessage());
            //e.printStackTrace();
        } catch (Exception e) {
            MyLogger.debug("Exception in makeVideoThumbnail " + e.getMessage());
            //e.printStackTrace();
        } finally {
            mmRetriever.release();
        }

    }

    public static String getVideoIconPath(String inputFilePath) {
        File f = new File(inputFilePath);
        String name = f.getName();
        String nameWithoutExT = FilenameUtils.removeExtension(name);
        return vaultGifIconFolder + "ic_" + nameWithoutExT + ".gif";
    }

    public static String makeVideoIconIfDoesNoExists(Context context, LockedVideoFiles file) {

        // create gif for videos and store in icons/gif folder ex gif/ic_NAME.gif
        String videoFilePath = file.getActualPath();
        File videoFile = new File(videoFilePath);
        String videoIconPath = ImagesVaultHelper.getVideoIconPath(videoFilePath);
        File iconFile = new File(videoIconPath);
        MyLogger.debug("Video File Path " + videoFilePath);
        MyLogger.debug("Video Icon Path " + videoIconPath);
        if (videoFile.exists()) {
            if (!iconFile.exists()) {
                makeVideoThumbnail(context, videoIconPath, videoFilePath);
            } else {
                MyLogger.debug("Video Gif file exists in makeVideoIconIfDoesNoExists");
            }
        } else {
            MyLogger.debug("Video file dos not exists in makeVideoIconIfDoesNoExists");
        }
        return videoIconPath;
    }
}

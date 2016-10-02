package com.spy.audiorecorder;

import android.content.ContentValues;
import android.media.MediaRecorder;
import android.provider.MediaStore;


/**
 * Created by milton on 7/03/16.
 */
public class AudioRecorderHelper {
    public static void recordAudio(String fileName) {
        final MediaRecorder recorder = new MediaRecorder();
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile("/sdcard/sound/" + fileName);
        recorder.start();
        recorder.stop();
        recorder.release();
    }
}

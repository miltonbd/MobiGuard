package com.spy.audiorecorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fs.lib.util.BaseDialogFragment;
import com.fs.lib.util.MessageHelper;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;
import com.util.Statics;


import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAudioRedcordDialogFragment extends BaseDialogFragment {
    private boolean isRecording=false;
    private boolean isAdd = true;
    private AudioRecordPojo item;
    private AudioRecorder audioRecorder;

    @Bind(R.id.buttonStart)
    Button buttonStart; // Also pause

    @Bind(R.id.buttonStop)
    Button buttonStop;

    @Bind(R.id.textViewAudioPath)
    TextView textViewAudioPath;

    @Bind(R.id.editTextFileName)
    EditText editTextFileName;


    public static AddAudioRedcordDialogFragment newInstanceForAdd() {
        AddAudioRedcordDialogFragment fragment = new AddAudioRedcordDialogFragment();
        fragment.setCurrentItem(new AudioRecordPojo());
        fragment.setIsAdd(true);
        return fragment;
    }

    public static AddAudioRedcordDialogFragment newInstanceForEdit(AudioRecordPojo editItem) {
        AddAudioRedcordDialogFragment fragment = new AddAudioRedcordDialogFragment();
        fragment.setCurrentItem(editItem);
        fragment.setIsAdd(false);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_audio_record, null);
        ButterKnife.bind(this, view);
        textViewAudioPath.setText(Statics.vaultAudioFolder);
        getDialog().setTitle("Add Audio Record");
        audioRecorder = new AudioRecorder();
        return view;
    }

    @OnClick(R.id.buttonStart)
    public void onButtonStartClicked(View view) {
        String fileName=editTextFileName.getText().toString();
        if (fileName.matches("")) {
            MessageHelper.showToast(getContext(), "File Name Caan ot be empty.");
            return;
        }
        String fullPath=textViewAudioPath.getText().toString()+fileName+".mp4";
        audioRecorder.setPath(fullPath);
        try {
            audioRecorder.start();
            MyLogger.debug("Audio Recording Started Successfully.");
            isRecording=true;
        } catch (IOException e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }

    }

    @OnClick(R.id.buttonStop)
    public void onButtonStopClicked(View view) {
        if (isRecording==true) {
            isRecording=false;
            try {
                audioRecorder.stop();
                MyLogger.debug("Audio Record ended Successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCurrentItem(AudioRecordPojo editItem) {
        this.item = editItem;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }
}
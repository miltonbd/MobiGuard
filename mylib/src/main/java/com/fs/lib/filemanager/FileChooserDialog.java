package com.fs.lib.filemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fs.lib.R;
import com.fs.lib.util.MyLogger;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by milton on 7/02/16.
 */
public class FileChooserDialog extends DialogFragment{

    private static FileChooserDialog instance;

    public static FileChooserDialog getInstance(){
        if (instance==null){
            instance=new FileChooserDialog();
        }
        return  instance;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_file_chooser_dialog, null);
        ButterKnife.bind(this, view);
        getDialog().setTitle("Choose a Picture.");


        ImageFileFinders ff=new ImageFileFinders();
        ArrayList<String> allImages= ff.getFileList();
        MyLogger.debugArray("All Images found ", allImages);

        return view;
    }

}

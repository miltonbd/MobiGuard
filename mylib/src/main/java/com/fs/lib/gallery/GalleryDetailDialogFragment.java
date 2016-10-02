package com.fs.lib.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fs.lib.R;

/**
 * Created by milton on 9/02/16.
 */
public class GalleryDetailDialogFragment extends DialogFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.gallery_detail,null);
        getDialog().setTitle("Image View");
        return v;
    }
}

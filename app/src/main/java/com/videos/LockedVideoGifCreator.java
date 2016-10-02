package com.videos;

import android.content.Context;
import android.os.AsyncTask;


import com.images.ImagesVaultHelper;

/**
 * Created by milton on 10/02/16.
 */
public class LockedVideoGifCreator extends AsyncTask<Void,Void,Void> {
    private Context context;
    private String actualPath;

    public LockedVideoGifCreator(Context context, String actualPath) {
        this.context = context;
        this.actualPath = actualPath;
    }
    @Override
    protected Void doInBackground(Void... params) {

        LockedVideoFiles lockedVideoFiles=new LockedVideoFiles();
        lockedVideoFiles.setActualPath(actualPath);
        ImagesVaultHelper.makeVideoIconIfDoesNoExists(context, lockedVideoFiles);

        return null;
    }
}

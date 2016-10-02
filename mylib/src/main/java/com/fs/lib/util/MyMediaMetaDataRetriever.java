package com.fs.lib.util;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by milton on 10/02/16.
 */
public class MyMediaMetaDataRetriever extends FFmpegMediaMetadataRetriever {
    private static MyMediaMetaDataRetriever instance;

    public static MyMediaMetaDataRetriever getInstance(){
       if (instance==null) {
           instance=new MyMediaMetaDataRetriever();
       }
        return instance;
    }
}

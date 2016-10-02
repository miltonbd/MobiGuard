package com.fs.lib.util;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoFileFinder {
        private final List<String> root;
        private ArrayList<String> fileList = new ArrayList<String>();

        public VideoFileFinder() {
            root = FileHelper.getStorages();
        }

        public ArrayList<String> getFileList() {
            for (String extPath :root) {
                getfile(new File(extPath));
            }
            MyLogger.debug("Total Video found " + fileList.size());
            return fileList;
        }
    public ArrayList<String> getLockedFileList() {
        for (String extPath :root) {
            getfile(new File(extPath));
        }
        MyLogger.debug("Total Video found " + fileList.size());
        return fileList;
    }

        /**
         * Getting All Images Path
         *
         * @param dir
         * @return ArrayList with images Path
         */

        private ArrayList<String> getfile(File dir) {
            File listFile[] = dir.listFiles();
            if (listFile != null && listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {

                    File file = listFile[i];
                    //todo not include mobiguard
                    boolean isBlackListed=file.getAbsolutePath().matches(".*Android.*")
                            ||file.getAbsolutePath().matches(".*__chartboost.*")
                            ||file.getAbsolutePath().matches(".*tencent.*");
                    if (file.isDirectory()&&!file.isHidden()&&!file.getName().matches("MobiGuard")&&!isBlackListed) {
                        //fileList.add(listFile[i]);
                        getfile(listFile[i]);

                    } else {
                        if (listFile[i].getName().endsWith(".3gp")
                                || listFile[i].getName().endsWith(".mp4")
                                || listFile[i].getName().endsWith(".avi")
                                || listFile[i].getName().endsWith(".wmp"))

                        {
                            fileList.add(listFile[i].getAbsolutePath());
                        }
                    }

                }
            }
            return fileList;
        }

}

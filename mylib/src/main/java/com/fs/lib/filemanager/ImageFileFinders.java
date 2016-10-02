package com.fs.lib.filemanager;



import com.fs.lib.util.MyLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageFileFinders {
    private final List<String> root;
    private ArrayList<String> fileList = new ArrayList<String>();

    public ImageFileFinders() {
        root = com.fs.lib.util.FileHelper.getStorages();
    }

    public ArrayList<String> getFileList() {
        for (String extPath :root) {
            getfile(new File(extPath));
        }
        MyLogger.debug("Total images found " + fileList.size());
        return fileList;
    }


    /**
     * Getting All Images Path
     *
     * @param activity
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
                    if (listFile[i].getName().endsWith(".png")
                            || listFile[i].getName().endsWith(".jpg")
                            || listFile[i].getName().endsWith(".jpeg")
                            || listFile[i].getName().endsWith(".gif"))

                    {
                        fileList.add(listFile[i].getAbsolutePath());
                    }
                }

            }
        }
        return fileList;
    }

}

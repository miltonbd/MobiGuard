package com.videos;

import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

/**
 * starting the app from service will list all videos and save the in db.
 * later when adding new video from db. service will add the videos when available.
 * add invalid to indicate which are invalid video files
 */
public class LockedVideoFiles extends TableBase {

    @DatabaseField
    private String actualPath; // need when to restore the file.

    @DatabaseField
    private String name;

    @DatabaseField
    private String path;

    @DatabaseField
    private Boolean isLocked=false;

    @DatabaseField
    private Boolean isInvalid=false;

    @DatabaseField
    private String iconPath; // icon and Thumbnail Path

    public Boolean getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(Boolean isInvalid) {
        this.isInvalid = isInvalid;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getActualPath() {
        return actualPath;
    }

    public void setActualPath(String actualPath) {
        this.actualPath = actualPath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @DatabaseField
    private String extension; // It can be used for hide extension


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LockedVideoFiles{" +
                "actualPath='" + actualPath + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", isLocked=" + isLocked +
                ", iconPath='" + iconPath + '\'' +
                ", extension='" + extension + '\'' +
                "} " + super.toString();
    }
}

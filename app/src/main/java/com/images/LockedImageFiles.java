package com.images;

import com.util.TableBase;
import com.j256.ormlite.field.DatabaseField;

public class LockedImageFiles extends TableBase {

    @DatabaseField
    private String name;

    @DatabaseField
    private String path;

    @DatabaseField
    private Boolean isLocked;

    @DatabaseField
    private String iconPath; // icon and Thumbnail Path

    @DatabaseField

    private String actualPath; // need when to restore the file.

    @DatabaseField
    private String extension; // It can be used for hide extension

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getActualPath() {
        return actualPath;
    }

    public void setActualPath(String actualPath) {
        this.actualPath = actualPath;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

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
        return "LockedImageFiles{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}

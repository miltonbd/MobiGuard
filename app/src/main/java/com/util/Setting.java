package com.util;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by milton on 9/03/16. default id 1 will be used
 */
public class Setting extends TableBase {
    @DatabaseField
    public Boolean isSpyCaallRecordiingActive=false;

    public Boolean getIsSpyCaallRecordiingActive() {
        return isSpyCaallRecordiingActive;
    }

    public void setIsSpyCaallRecordiingActive(Boolean isSpyCaallRecordiingActive) {
        this.isSpyCaallRecordiingActive = isSpyCaallRecordiingActive;
    }
}

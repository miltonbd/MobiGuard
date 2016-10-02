package com.fs.lib.menu;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by milton on 20/02/16.
 */
public class BottomMenu extends LinearLayout {

    public BottomMenu(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        isInEditMode();
    }

}

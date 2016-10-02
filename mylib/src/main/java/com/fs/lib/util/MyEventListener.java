package com.fs.lib.util;

import android.view.View;
import android.widget.CompoundButton;

public interface MyEventListener {
    public void onItemClick(int position, View v);
    public void onItemLongClick(int position, View v);
    public void onItemCheckedChange(int position, CompoundButton button, boolean checked);
}
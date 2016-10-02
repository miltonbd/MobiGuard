package com.applock;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.ViewHelper;

import java.util.List;

public class ListLockedAppAdapter extends BaseMultipleSelectAdapter<LockedApps> {

    public static class MyItemHolder extends DataObjectHolder  {
        TextView textViewSerialNo;
        TextView textViewAppName;
        ImageView imageViewAppIcon;
        private View itemView;
        public MyItemHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ViewHelper.addHover(this.itemView);
            textViewSerialNo = (TextView) itemView.findViewById(com.mobiguard.R.id.textViewSerialNo);
            textViewAppName = (TextView) itemView.findViewById(com.mobiguard.R.id.textViewAppName);
            imageViewAppIcon = (ImageView) itemView.findViewById(com.mobiguard.R.id.imageViewAppIcon);
            this.itemView.setOnClickListener(this);
        }
    }

    public ListLockedAppAdapter(List<LockedApps> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj= (MyItemHolder) holder;
        LockedApps file = items.get(position);
        obj.textViewSerialNo.setText(String.valueOf(position + 1+". "));
        obj.textViewAppName.setText(file.getAppName());
        MyLogger.debug("Locked App item " + file);
        Context context=obj.imageViewAppIcon.getContext();
        Drawable icon = null;
        try {
            Drawable app=  context.getPackageManager().getApplicationIcon(file.getPackageName());
            Glide.with(context)
                    .load("")
                    .placeholder(app)
                    .into(((MyItemHolder) holder).imageViewAppIcon);
            if (isPositionChecked(position)){
                ((MyItemHolder) holder).itemView.setBackgroundColor(context.getResources().getColor(com.mobiguard.R.color.blue));
            }else {
                ((MyItemHolder) holder).itemView.setBackgroundColor(context.getResources().getColor(com.mobiguard.R.color.white));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DataObjectHolder createDataObject(View views) {
        MyItemHolder viewHolder = new MyItemHolder(views);
        return viewHolder;
    }
}



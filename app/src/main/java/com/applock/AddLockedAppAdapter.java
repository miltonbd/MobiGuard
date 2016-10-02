package com.applock;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.ViewHelper;
import com.mobiguard.R;

import java.util.List;

public class AddLockedAppAdapter extends BaseMultipleSelectAdapter<AppInfoPojo> {

    public static class MyItemHolder extends DataObjectHolder {
        TextView textViewSerialNo;
        TextView textViewPackageName;
        ImageView imageViewAppIcon;
        private View itemView;

        public MyItemHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ViewHelper.addHover(this.itemView);
            textViewSerialNo = (TextView) itemView.findViewById(R.id.textViewSerialNo);
            textViewPackageName = (TextView) itemView.findViewById(R.id.textViewPackageName);
            imageViewAppIcon = (ImageView) itemView.findViewById(R.id.imageViewAppIcon);
            this.itemView.setOnClickListener(this);
        }
    }

    public AddLockedAppAdapter(List<AppInfoPojo> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj = (MyItemHolder) holder;
        AppInfoPojo item = items.get(position);
        MyLogger.debug("App item in AddLockedAppAdapter " + item);
        obj.textViewPackageName.setText(item.getAppName());
        obj.textViewSerialNo.setText(String.valueOf(position + 1 + ". "));
        Context context = obj.textViewSerialNo.getContext();
        Glide.with(context)
                .load("")
                .placeholder(item.getIconDrawable())
                .into(((MyItemHolder) holder).imageViewAppIcon);
        if (isPositionChecked(position)){
            ((MyItemHolder) holder).itemView.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }else {
            ((MyItemHolder) holder).itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public DataObjectHolder createDataObject(View views) {
        MyItemHolder viewHolder = new MyItemHolder(views);
        return viewHolder;
    }
}



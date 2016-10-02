package com.videos;


import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.images.ImagesVaultHelper;
import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;

import java.util.List;

public class AddLockedVideoAdapter extends BaseMultipleSelectAdapter<LockedVideoFiles> {

    public static class MyItemHolder extends DataObjectHolder  {
        ImageView mImg;
        ImageView item_selected_img;
        public MyItemHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            item_selected_img = (ImageView) itemView.findViewById(R.id.item_selected_img);
        }
    }

    public AddLockedVideoAdapter(List<LockedVideoFiles> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj= (MyItemHolder) holder;
        LockedVideoFiles file = items.get(position);
        MyLogger.debug("Video item in AddLockedVideoAdapter " + file);
        String gifIconPath= ImagesVaultHelper.makeVideoIconIfDoesNoExists(obj.mImg.getContext(), file);
        Glide.with(obj.mImg.getContext()).load(gifIconPath)
                .thumbnail(0.5f)
                .override(200,200)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((MyItemHolder) holder).mImg);
        if (selectedItems.containsKey(position)){
            MyLogger.debug("Selected Item Found.");
            obj.mImg.setAlpha(180);
            obj.item_selected_img.setVisibility(View.VISIBLE);
        }else {
            obj.mImg.setAlpha(255);
            obj.item_selected_img.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public DataObjectHolder createDataObject(View views) {
        MyItemHolder viewHolder = new MyItemHolder(views);
        return viewHolder;
    }

}



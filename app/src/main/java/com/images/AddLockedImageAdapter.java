package com.images;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fs.lib.R;
import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.MyLogger;

import java.util.List;

public class AddLockedImageAdapter extends BaseMultipleSelectAdapter<LockedImageFiles> {

    public static class MyItemHolder extends DataObjectHolder  {
        ImageView mImg;
        ImageView item_selected_img;
        public MyItemHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.item_img);
            item_selected_img = (ImageView) itemView.findViewById(R.id.item_selected_img);
        }
    }

    public AddLockedImageAdapter(List<LockedImageFiles> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj= (MyItemHolder) holder;
        Glide.with(obj.mImg.getContext()).load(items.get(position).getPath())
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



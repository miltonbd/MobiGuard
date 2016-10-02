package com.videos;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.FileHelper;
import com.fs.lib.util.MyLogger;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ListLockedVideoFilesAdapter extends BaseMultipleSelectAdapter<LockedVideoFiles> {
    public static class MyItemHolder extends DataObjectHolder  {
        ImageView mImg;
        ImageView item_selected_img;

        public MyItemHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(com.fs.lib.R.id.item_img);
            item_selected_img = (ImageView) itemView.findViewById(com.fs.lib.R.id.item_selected_img);
        }
    }


    public ListLockedVideoFilesAdapter(List<LockedVideoFiles> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj= (MyItemHolder) holder;
        LockedVideoFiles lockedVideoFile = items.get(position);
        MyLogger.debug("Locked Video File in ListLockedVideoFilesAdapter "+lockedVideoFile);
        Glide.with(obj.mImg.getContext()).load(lockedVideoFile.getIconPath())
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


    public void restoreSelectedItems(MyDatabaseHelper db) throws SQLException {
        if (selectedItems.size()>0) {
            for (Map.Entry<Integer, Boolean> selectedItem : selectedItems.entrySet()) {
                LockedVideoFiles item= getItem(selectedItem.getKey());
                FileHelper.deleteFile(item.getIconPath());
                deleteFromDb(item, db);
                FileHelper.moveFile(item.getPath(), item.getActualPath());

            }
        }
    }

    public void deleteSelectedItems(MyDatabaseHelper db) throws SQLException {
        if (selectedItems.size()>0) {
            for (Map.Entry<Integer, Boolean> selectedItem : selectedItems.entrySet()) {
                LockedVideoFiles item= getItem(selectedItem.getKey());
                FileHelper.deleteFile(item.getIconPath());
                deleteFromDb(item, db);
                FileHelper.deleteFile(item.getPath());
            }
        }
    }

    private void deleteFromDb(LockedVideoFiles file
            ,MyDatabaseHelper db) throws SQLException {
       UpdateBuilder<LockedVideoFiles, Long> ub= db.getLockedVideosFilesDao().updateBuilder();
        ub.updateColumnValue("isLocked",false);
        ub.where().eq("id",file.getId());
        ub.update();
    }

}
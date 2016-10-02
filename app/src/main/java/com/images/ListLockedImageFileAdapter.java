package com.images;


import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.FileHelper;
import com.fs.lib.util.MyLogger;
import com.util.MyDatabaseHelper;
import com.videos.LockedVideoFiles;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ListLockedImageFileAdapter extends BaseMultipleSelectAdapter<LockedImageFiles> {
    public static class MyItemHolder extends DataObjectHolder  {
        ImageView mImg;
        ImageView item_selected_img;
        public MyItemHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(com.fs.lib.R.id.item_img);
            item_selected_img = (ImageView) itemView.findViewById(com.fs.lib.R.id.item_selected_img);
        }
    }


    public ListLockedImageFileAdapter(List<LockedImageFiles> items, int resource) {
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

    /**
     * to restore, update the column that has locked.
     * @param db
     * @throws SQLException
     */
    public void restoreSelectedItems(MyDatabaseHelper db) throws SQLException {
        if (selectedItems.size()>0) {
            for (Map.Entry<Integer, Boolean> selectedItem : selectedItems.entrySet()) {
                LockedImageFiles lockedImageFile= getItem(selectedItem.getKey());
                FileHelper.deleteFile(lockedImageFile.getIconPath());
                deleteFromDb(lockedImageFile,db);
                FileHelper.moveFile(lockedImageFile.getPath(),lockedImageFile.getActualPath());
            }
        }
    }

    public void deleteSelectedItems(MyDatabaseHelper db) throws SQLException {
        if (selectedItems.size()>0) {
            for (Map.Entry<Integer, Boolean> selectedItem : selectedItems.entrySet()) {
                LockedImageFiles lockedImageFile= getItem(selectedItem.getKey());
                FileHelper.deleteFile(lockedImageFile.getIconPath());
                deleteFromDb(lockedImageFile, db);
                FileHelper.deleteFile(lockedImageFile.getPath());
            }
        }
    }

    private void deleteFromDb(LockedImageFiles lockedImageFile,MyDatabaseHelper db) throws SQLException {
        Dao<LockedImageFiles, Long> dao= db.getLockedImageFilesDao();
        dao.deleteById(lockedImageFile.getId());
    }

    private void unLockVideo(LockedVideoFiles lockedVideoFiles,MyDatabaseHelper db) throws SQLException {
        UpdateBuilder<LockedImageFiles, Long> ub= db.getLockedImageFilesDao().updateBuilder();
        ub.updateColumnValue("isLocked",false);
        ub.where().eq("id",lockedVideoFiles.getId());
        ub.update();
    }

}
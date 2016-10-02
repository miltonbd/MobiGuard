package com.applock;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fs.lib.util.MyLogger;
import com.mobiguard.R;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddAppLockAdapter extends RecyclerView
        .Adapter<AddAppLockAdapter
        .DataObjectHolder> {
    private SparseBooleanArray selectedItems;
    private boolean isCABActive = false;
    private List<LockedApps> items;
    private boolean isBusy = false;
    private MyDatabaseHelper db;

    public AddAppLockAdapter(MyDatabaseHelper db) {
        this.db = db;
    }

    class MyCAB implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            AddAppLockAdapter.this.isCABActive = true;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            MyLogger.debug("Context action menu item clicked.");
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            AddAppLockAdapter.this.isCABActive = false;
        }
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder
             {
        View view;
        TextView no;
        ImageView imageView;
        TextView categoryName;
        CheckBox checkBox;
        boolean isLocked = false;

        public DataObjectHolder(View itemView) {
            super(itemView);
            view = itemView;
            no = (TextView) itemView.findViewById(R.id.textViewSerialNo);
            categoryName = (TextView) itemView.findViewById(R.id.textView2);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            checkBox.setFocusable(false);
            checkBox.setClickable(false);
        }

        public void onCheckedChanged(boolean isChecked) {
            LockedApps lockedApps = (LockedApps) checkBox.getTag();
            MyLogger.debug("onChecked LockedApp " + lockedApps);
            View parentView = (View) checkBox.getParent();
            try {
                checkBox.setChecked(isChecked);
                Dao<LockedApps, Long> lockedAppsDao = db.getLockedAppsDao();

                if (isChecked) {
                    MyLogger.debug("Creating Locked App with " + lockedApps);
                    lockedAppsDao.create(lockedApps);
                    parentView.setBackgroundColor(checkBox.getContext().getResources().getColor(R.color.blue));
                    MyLogger.debug("App Added To Locker.");

                } else {
                    parentView.setBackgroundColor(checkBox.getContext().getResources().getColor(R.color.white));
                    MyLogger.debug("Deleting Locked App with " + lockedApps);
                    DeleteBuilder<LockedApps, Long> deleteBuilder = lockedAppsDao.deleteBuilder();
                    deleteBuilder.where().eq("id", lockedApps.getId());
                    deleteBuilder.delete();
                    //Show popup or context menu as dialog
                    MyLogger.debug("App Released from Locker.");
                }
            } catch (SQLException e) {
                MyLogger.debug(e.getMessage());
            }

        }
    }


    public AddAppLockAdapter(List<LockedApps> items) {
        this.items = items;
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }


    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_applock_list, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        LockedApps item = items.get(position);
        holder.categoryName.setText(item.getPackageName());
        ++position;
        holder.no.setText(String.valueOf(position) + ". ");
      //  holder.imageView.setImageDrawable(holder.categoryName.getContext().getDrawable(item.getIcon()));
        if (items != null) {
            for (LockedApps lockedApp : items) {
                holder.checkBox.setChecked(true);
                holder.view.setBackgroundColor(holder.view.getContext().getResources().getColor(R.color.blue));
                holder.isLocked = true;
                holder.checkBox.setTag(lockedApp);
            }
        }

    }

    public void addItem(LockedApps dataObj, int index) {
        items.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        items.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
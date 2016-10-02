package com.fs.lib.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by milton on 8/02/16.
 */
public abstract class BaseMultipleSelectAdapter<E> extends  RecyclerView.Adapter<BaseMultipleSelectAdapter.DataObjectHolder> {
    private int resource;
    private Context context;
    public List<E> items = new ArrayList<E>();
    public HashMap<Integer,Boolean> selectedItems=new HashMap<Integer,Boolean>();
    private boolean isCABActive = false;
    private static  MyEventListener myClickListener;
    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        public DataObjectHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(myClickListener!=null){
                myClickListener.onItemClick(getPosition(),v);
                MyLogger.debug("myClickListener is not null in "+myClickListener);
            }else {
                MyLogger.debug("myClickListener is null in "+getClass().getName());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(myClickListener!=null){
                myClickListener.onItemLongClick(getPosition(), v);
            }
            return true;
        }
    }

    public static MyEventListener getMyClickListener() {
        return myClickListener;
    }

    public static void setMyClickListener(MyEventListener myClickListener) {
        BaseMultipleSelectAdapter.myClickListener = myClickListener;
    }

    public BaseMultipleSelectAdapter(List<E> items, int resource) {
        this.context = context;
        this.items = items;
        this.resource = resource;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);

        DataObjectHolder dataObjectHolder = createDataObject(view);
        return dataObjectHolder;
    }

    public void setMyEventListener(MyEventListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public abstract DataObjectHolder createDataObject(View views);

    public void toggleSelection(int pos) {
        if (selectedItems.containsKey(pos)) {
            selectedItems.remove(pos);
        } else {
            selectedItems.put(pos, true);
        }
        MyLogger.debug("Total items selected count "+selectedItems.size());
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
        List<Integer> itemsPositions =
                new ArrayList<Integer>(selectedItems.size());
        for (Map.Entry<Integer, Boolean> item:selectedItems.entrySet()) {
            itemsPositions.add(item.getKey());
        }
        return itemsPositions;
    }
    public void addItem(E dataObj, int index) {
        items.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        items.remove(index);
        notifyItemRemoved(index);
    }

    public void setNewSelection(int position, boolean value) {
        selectedItems.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = selectedItems.get(position);
        return result == null ? false : result;
    }

    /*public Set<Integer> getCurrentCheckedPosition() {
        return selectedItems.keySet();
    }*/

    public void removeSelection(int position) {
        selectedItems.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }
    public E getItem(int position) {
        return items.get(position);
    }
    @Override
public int getItemCount() {
        return items.size();
        }

}

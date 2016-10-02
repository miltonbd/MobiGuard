package com.callsms.calls;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.ViewHelper;
import com.mobiguard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Do not refactior the class.
 */
public class BlockedNumberAdapter extends  RecyclerView.Adapter<BlockedNumberAdapter.DataObjectHolder> {
    private int resource;
    private Context context;
    public List<BlockedCallSMSNumber> items = new ArrayList<BlockedCallSMSNumber>();
    public HashMap<Integer,Boolean> selectedItems=new HashMap<Integer,Boolean>();
    private boolean isCABActive = false;
    private static MyEventListener myClickListener;
    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView textViewSerialNo;
        TextView textViewNumber;
        private View itemView;
        CheckBox checkBoxBlockCall;
        CheckBox checkBoxBlockSMS;
        public DataObjectHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ViewHelper.addHover(this.itemView);
            textViewSerialNo = (TextView) itemView.findViewById(R.id.textViewSerialNo);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
            checkBoxBlockCall= (CheckBox) itemView.findViewById(R.id.checkBoxBlockCall);
            checkBoxBlockSMS= (CheckBox) itemView.findViewById(R.id.checkBoxBlockSMS);

            checkBoxBlockCall.setFocusable(false);
            checkBoxBlockCall.setClickable(false);

            checkBoxBlockSMS.setFocusable(false);
            checkBoxBlockSMS.setClickable(false);
        }

        @Override
        public void onClick(View v) {
            if(myClickListener!=null){
                myClickListener.onItemClick(getPosition(),v);
                MyLogger.debug("myClickListener is not null in " + myClickListener);
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
        BlockedNumberAdapter.myClickListener = myClickListener;
    }

    public BlockedNumberAdapter(List<BlockedCallSMSNumber> items, int resource) {
        this.context = context;
        this.items = items;
        this.resource = resource;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        BlockedCallSMSNumber item = items.get(position);
        MyLogger.debug("App item in AddLockedAppAdapter " + item);
        holder.textViewSerialNo.setText(String.valueOf(position + 1 + ". "));
        Context context = holder.textViewSerialNo.getContext();
        if (isPositionChecked(position)){
             holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.textViewNumber.setText(item.getBlockedNumber());
        holder.checkBoxBlockCall.setChecked(item.getIsCallBlocked());
        holder.checkBoxBlockSMS.setChecked(item.getIsSMSBlocked());
    }

    public void setMyEventListener(MyEventListener myClickListener) {
        this.myClickListener = myClickListener;
    }

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
    public void addItem(BlockedCallSMSNumber dataObj, int index) {
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
    public BlockedCallSMSNumber getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

}

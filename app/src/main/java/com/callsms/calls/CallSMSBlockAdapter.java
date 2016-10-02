package com.callsms.calls;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;

import java.util.List;

public class CallSMSBlockAdapter extends BaseMultipleSelectAdapter<BlockedCallSMSNumber> {
    public static class MyItemHolder extends DataObjectHolder {
        TextView textViewSerialNo;
        TextView textViewNumber;


        public MyItemHolder(View itemView) {
            super(itemView);
            textViewSerialNo = (TextView) itemView.findViewById(R.id.textViewSerialNo);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
        }
    }

    public CallSMSBlockAdapter(List<BlockedCallSMSNumber> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj = (MyItemHolder) holder;
        Context context = obj.textViewSerialNo.getContext();
        obj.textViewSerialNo.setText(String.valueOf(position + 1 + ". "));

        BlockedCallSMSNumber item = items.get(position);
        obj.textViewNumber.setText(item.getBlockedNumber());

        LinearLayout v= (LinearLayout) obj.textViewSerialNo.getParent();
        MyLogger.debug("total selected items count " + selectedItems.size());

        if (selectedItems.containsKey(position)) {
            v.setBackgroundColor(context.getResources().getColor(R.color.blue));
            MyLogger.debug("Selecting item in " + getClass().getName());
        } else {
            v.setBackgroundColor(context.getResources().getColor(R.color.white));
            MyLogger.debug("DeSelecting item in " + getClass().getName());
        }

    }

    @Override
    public DataObjectHolder createDataObject(View views) {
        MyItemHolder viewHolder = new MyItemHolder(views);
        return viewHolder;
    }
}



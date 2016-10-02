package com.callsms.calls;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;


import java.util.List;

public class CallLogAdapter extends BaseMultipleSelectAdapter<CallLogPojo> {
    public static class MyItemHolder extends DataObjectHolder {
        TextView textViewSerialNo;
        TextView textViewNumber;
        TextView textViewDurtaion;
        TextView textViewDate;
        TextView textViewTime;
        CheckBox checkBox;
        ImageView imageViewCallType;


        public MyItemHolder(View itemView) {
            super(itemView);
            textViewSerialNo = (TextView) itemView.findViewById(R.id.textViewSerialNo);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewDurtaion = (TextView) itemView.findViewById(R.id.textViewDurtaion);
            textViewDate = (TextView) itemView.findViewById(R.id.textxViewDate);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
            imageViewCallType = (ImageView) itemView.findViewById(R.id.imageViewCallType);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            checkBox.setFocusable(false);
            checkBox.setClickable(false);

        }
    }

    public CallLogAdapter(List<CallLogPojo> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj = (MyItemHolder) holder;
        Context context = obj.textViewSerialNo.getContext();
        CallLogPojo item = items.get(position);
        obj.textViewSerialNo.setText(String.valueOf(position) + ". ");
        obj.textViewNumber.setText(item.getPhoneNumber());

        obj.textViewDurtaion.setText(item.getDuration());

        obj.textViewDate.setText(item.getDateTime());

        if (item.getCallType() == CallLogPojoType.DIALLED) {
            Glide.with(context).load(R.drawable.ic_call_made_black_48dp).into(obj.imageViewCallType);
        }
        if (item.getCallType() == CallLogPojoType.RECEIVED) {
            Glide.with(context).load(R.drawable.ic_call_received_black_48dp).into(obj.imageViewCallType);
        }
        if (item.getCallType() == CallLogPojoType.MISSED) {
            Glide.with(context).load(R.drawable.ic_call_missed_black_48dp).into(obj.imageViewCallType);
        }
        LinearLayout v= (LinearLayout) obj.textViewSerialNo.getParent();
        MyLogger.debug("total selected items count "+selectedItems.size());
        if (selectedItems.containsKey(position)) {
            v.setBackgroundColor(context.getResources().getColor(R.color.blue));
            MyLogger.debug("Selecting item in " + getClass().getName());
            obj.checkBox.setChecked(true);
        } else {
            v.setBackgroundColor(context.getResources().getColor(R.color.white));
            obj.checkBox.setChecked(false);
            MyLogger.debug("DeSelecting item in " + getClass().getName());
        }

    }

    @Override
    public DataObjectHolder createDataObject(View views) {
        MyItemHolder viewHolder = new MyItemHolder(views);
        return viewHolder;
    }
}



package com.callsms.sms;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;

import java.util.List;

public class SMSLogAdapter extends BaseMultipleSelectAdapter<SmsPojo> {
    public static class MyItemHolder extends DataObjectHolder {
        TextView textViewSerialNo;
        TextView textViewMessage;
        TextView textViewNumber;
        TextView textViewDurtaion;
        TextView textViewDate;
        TextView textViewTime;
        ImageView imageViewCallType;


        public MyItemHolder(View itemView) {
            super(itemView);
            textViewSerialNo = (TextView) itemView.findViewById(R.id.textViewSerialNo);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewDurtaion = (TextView) itemView.findViewById(R.id.textViewDurtaion);
            textViewDate = (TextView) itemView.findViewById(R.id.textxViewDate);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
            imageViewCallType = (ImageView) itemView.findViewById(R.id.imageViewCallType);
            textViewMessage = (TextView) itemView.findViewById(R.id.texxtViewMessage);

        }
    }

    public SMSLogAdapter(List<SmsPojo> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj = (MyItemHolder) holder;
        Context context = obj.textViewSerialNo.getContext();
        SmsPojo item = items.get(position);
        obj.textViewMessage.setText(item.getMsg());
        MyLogger.debug("SMSPojo " + item);
        obj.textViewSerialNo.setText(String.valueOf(position) + ". ");
        obj.textViewNumber.setText(item.getAddress());
        obj.textViewDate.setText(item.getDateTime());

        if (item.getFolderName() == SMSLogType.SENT) {
            Glide.with(context).load(R.drawable.ic_call_made_black_48dp).into(obj.imageViewCallType);
        }
        if (item.getFolderName() == SMSLogType.INBOX) {
            Glide.with(context).load(R.drawable.ic_call_received_black_48dp).into(obj.imageViewCallType);
        }

        LinearLayout v= (LinearLayout) obj.textViewSerialNo.getParent();
        MyLogger.debug("total selected items count "+selectedItems.size());
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

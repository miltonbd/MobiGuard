package com.callsms.calls;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.callsms.contact.ContactPojo;
import com.fs.lib.util.BaseMultipleSelectAdapter;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;

import java.util.List;

public class ContactAdapter extends BaseMultipleSelectAdapter<ContactPojo> {
    public static class MyItemHolder extends DataObjectHolder {
        TextView textViewSerialNo;
        TextView textViewNumber;
        TextView textViewName;


        public MyItemHolder(View itemView) {
            super(itemView);
            textViewSerialNo = (TextView) itemView.findViewById(R.id.textViewSerialNo);
            textViewNumber = (TextView) itemView.findViewById(R.id.textViewNumber);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        }
    }

    public ContactAdapter(List<ContactPojo> items, int resource) {
        super(items, resource);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MyItemHolder obj = (MyItemHolder) holder;
        Context context = obj.textViewSerialNo.getContext();
        ContactPojo item = items.get(position);
        obj.textViewSerialNo.setText(String.valueOf(position+1) + ". ");
        obj.textViewNumber.setText(item.getNumber());
        obj.textViewName.setText(item.getName());

        RelativeLayout v= (RelativeLayout) obj.textViewSerialNo.getParent();
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



package com.fs.lib.keypad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.fs.lib.R;

import java.util.List;

public class NumKeypadAdapter extends ArrayAdapter<NUM_KEYS> {

    public NumKeypadAdapter(Context context, int resource, List<NUM_KEYS> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_num_keypad, parent, false);
        TextView tv= (TextView) view.findViewById(R.id.numPadKeyId);
        tv.setText(getItem(position).getKeyValue());
        return view;
    }
}
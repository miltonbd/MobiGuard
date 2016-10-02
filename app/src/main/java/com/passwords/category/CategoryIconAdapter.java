package com.passwords.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mobiguard.R;

import java.util.List;

public class CategoryIconAdapter extends ArrayAdapter<Integer> {

    public CategoryIconAdapter(Context context, int resource, List<Integer> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_password_category_icon, parent, false);
        ImageView img = (ImageView) view.findViewById(R.id.iconCategory);
        Glide.with(getContext()).load(getItem(position)).into(img);
        return view;
    }
}
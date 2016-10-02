package com.passwords.password;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobiguard.MyApp;
import com.mobiguard.R;
import com.passwords.category.PasswordCategory;

import java.util.List;

/***** Adapter class extends with ArrayAdapter ******/
public class AddPasswordSelectCategoryAdapter extends ArrayAdapter<PasswordCategory>{
    private List<PasswordCategory> data;
    public Resources res;
    LayoutInflater inflater;

    public AddPasswordSelectCategoryAdapter(Context context, int resource, List<PasswordCategory> objects) {
        super(context, resource, objects);
        this.data=objects;
    }


    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = MyApp.getMainActivity().getLayoutInflater().inflate(R.layout.list_item_select_password_category, parent, false);

         
        TextView label        = (TextView)row.findViewById(R.id.textViewSerialNo);

            // Set values for spinner each row
        PasswordCategory item = getItem(position);
        label.setText(item.getName());
         ImageView imageView= (ImageView) row.findViewById(R.id.imageView);
        imageView.setImageResource(item.getIcon().intValue());
        return row;
      }
 }
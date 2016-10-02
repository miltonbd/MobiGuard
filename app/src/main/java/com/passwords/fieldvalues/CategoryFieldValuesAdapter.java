package com.passwords.fieldvalues;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.mobiguard.R;

import java.util.List;

public class CategoryFieldValuesAdapter extends RecyclerView
        .Adapter<CategoryFieldValuesAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ListLockedVideoFilesAdapter";
    private List<PasswordFields> fields;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
       public TextView textViewFieldName;
       public EditText editTextFieldValue;

        public DataObjectHolder(View itemView) {
            super(itemView);
            textViewFieldName = (TextView) itemView.findViewById(R.id.textViewFieldName);
            editTextFieldValue = (EditText) itemView.findViewById(R.id.editTextFieldValue);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CategoryFieldValuesAdapter(List<PasswordFields> fields) {
        this.fields = fields;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_password_field_values, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
       PasswordFields passwordFields= fields.get(position);
        holder.textViewFieldName.setText(passwordFields.getName());
        if (passwordFields.getFieldValue()!=null){
            PasswordFieldValues passwordFieldValues=passwordFields.getFieldValue();
                holder.editTextFieldValue.setText(passwordFieldValues.getValue());
        }
        // find field v alue from FieldValues table
    }
 
    public void addItem(PasswordFields dataObj, int index) {
        fields.add(dataObj);
        notifyItemInserted(index);
    }
 
    public void deleteItem(int index) {
        fields.remove(index);
        notifyItemRemoved(index);
    }
 
    @Override
    public int getItemCount() {
        return fields.size();
    }
 
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
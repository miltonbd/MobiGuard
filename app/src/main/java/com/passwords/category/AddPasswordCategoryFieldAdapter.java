package com.passwords.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fs.lib.util.ViewHelper;
import com.mobiguard.R;
import com.passwords.fieldvalues.PasswordFields;

import java.util.List;

public class AddPasswordCategoryFieldAdapter extends RecyclerView
        .Adapter<AddPasswordCategoryFieldAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ListLockedVideoFilesAdapter";
    private List<PasswordFields> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener, View.OnFocusChangeListener {
        TextView textViewFieldName;
        ImageView imageViewUp;
        ImageView imageViewDown;
        ImageView imageViewDelete;
        ImageView imageViewEdit;
        EditText editTextFieldName;

        public DataObjectHolder(View itemView) {
            super(itemView);
            textViewFieldName = (TextView) itemView.findViewById(R.id.textViewFieldName);
            editTextFieldName = (EditText) itemView.findViewById(R.id.editTextFieldName);
            imageViewUp= (ImageView) itemView.findViewById(R.id.imageViewUp);
            imageViewDown= (ImageView) itemView.findViewById(R.id.imageViewDown);
            imageViewDelete= (ImageView) itemView.findViewById(R.id.imageViewDelete);
            imageViewEdit= (ImageView) itemView.findViewById(R.id.imageViewEdit);

            textViewFieldName.setOnClickListener(this);
            imageViewEdit.setOnClickListener(this);
            editTextFieldName.setOnFocusChangeListener(this);

            imageViewUp.setOnClickListener(this);
            imageViewDown.setOnClickListener(this);
            imageViewDelete.setOnClickListener(this);

            ViewHelper.addHover(textViewFieldName);
            ViewHelper.addHover(editTextFieldName);
            ViewHelper.addHover(imageViewUp);
            ViewHelper.addHover(imageViewDown);
            ViewHelper.addHover(imageViewDelete);
            ViewHelper.addHover(imageViewEdit);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            myClickListener.onOnFocusChanged(getPosition(),v,hasFocus);
        }
    }


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public AddPasswordCategoryFieldAdapter(List<PasswordFields> myDataset) {
        mDataset = myDataset;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category_field, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
       PasswordFields passwordCategoryFields= mDataset.get(position);
        holder.textViewFieldName.setText(passwordCategoryFields.getName());
    }
 
    public void addItem(PasswordFields dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }
 
    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);

    }
 
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
 
    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public void onOnFocusChanged(int position, View v, boolean hasFocus);
    }
}
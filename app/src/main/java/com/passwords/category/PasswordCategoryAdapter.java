package com.passwords.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fs.lib.util.ViewHelper;
import com.mobiguard.MyApp;
import com.mobiguard.R;

import java.util.List;

public class PasswordCategoryAdapter extends RecyclerView
        .Adapter<PasswordCategoryAdapter
        .DataObjectHolder> {
    private List<PasswordCategory> mDataset;
    private static MyClickListener myClickListener;
 
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView no;
        TextView categoryName;
        ImageView imageViewCategoryIcon;
        ImageView imageViewUp;
        ImageView imageViewDown;
        ImageView imageViewDelete;
        LinearLayout linearLayoutPasswordCategoryList;
 
        public DataObjectHolder(View itemView) {
            super(itemView);
            linearLayoutPasswordCategoryList= (LinearLayout) itemView.findViewById(R.id.linearLayoutPasswordCategoryList);
            no = (TextView) itemView.findViewById(R.id.textViewSerialNo);
            categoryName = (TextView) itemView.findViewById(R.id.textView2);
            imageViewUp= (ImageView) itemView.findViewById(R.id.imageViewUp);
            imageViewDown= (ImageView) itemView.findViewById(R.id.imageViewDown);
            imageViewDelete= (ImageView) itemView.findViewById(R.id.imageViewDelete);
            imageViewUp.setOnClickListener(this);
            imageViewDown.setOnClickListener(this);
            imageViewCategoryIcon= (ImageView) itemView.findViewById(R.id.imageViewCategoryIcon);
            linearLayoutPasswordCategoryList.setOnClickListener(this);
            imageViewDelete.setOnClickListener(this);
            ViewHelper.addHover(imageViewUp);
            ViewHelper.addHover(imageViewDown);
            ViewHelper.addHover(imageViewDelete);
            ViewHelper.addHover(linearLayoutPasswordCategoryList);
        }
 
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }
 
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
 
    public PasswordCategoryAdapter(List<PasswordCategory> myDataset) {
        mDataset = myDataset;
    }
 
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_password_category_list, parent, false);
 
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
 
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        PasswordCategory passwordCategory = mDataset.get(position);
        holder.categoryName.setText(passwordCategory.getName());
        holder.no.setText(String.valueOf(++position) + ". ");
        if (passwordCategory.getIcon()!=null)
        Glide.with(MyApp.getContext()).load(passwordCategory.getIcon().intValue()).into(holder.imageViewCategoryIcon);
    }
 
    public void addItem(PasswordCategory dataObj, int index) {
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
    }
}
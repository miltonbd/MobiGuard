package com.passwords.password;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fs.lib.util.ViewHelper;
import com.mobiguard.R;
import com.passwords.category.PasswordCategory;
import com.util.MyDatabaseHelper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class PasswordExpandableListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private Context _context;
    private   MyDatabaseHelper db;
    private List<PasswordCategory> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<PasswordCategory, List<Passwords>> _listDataChild;

    public PasswordExpandableListAdapter(Context context, List<PasswordCategory> listDataHeader,
                                         HashMap<PasswordCategory, List<Passwords>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        db = MyDatabaseHelper.getInstance(_context);
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }


 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final Passwords child = (Passwords) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandible_password_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        LinearLayout linearLayoutChild= (LinearLayout) convertView.findViewById(R.id.linearLayoutChild);

        ImageView imageViewAddPasswordInCategory = (ImageView) convertView
                .findViewById(R.id.imageViewAddPasswordInCategory);
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.imageView);
        ImageView imageViewUp= (ImageView) convertView.findViewById(R.id.imageViewUp);
        ImageView imageViewDown= (ImageView) convertView.findViewById(R.id.imageViewDown);
        ImageView imageViewDelete= (ImageView) convertView.findViewById(R.id.imageViewDelete);
        imageViewDown.setTag(child);
        imageViewUp.setTag(child);
        imageViewDelete.setTag(child);
        imageViewAddPasswordInCategory.setTag(child);
        linearLayoutChild.setTag(child);
        child.setGroupPosition(groupPosition);
        // Test if it is placeholder
        if (child.getTitle().matches("No item")) {
         LinearLayout layoutOperatiopns= (LinearLayout) convertView.findViewById(R.id.layoutOperatiopns);
            //layoutOperatiopns.setVisibility(View.GONE);
            imageViewDown.setVisibility(View.GONE);
            imageViewUp.setVisibility(View.GONE);
            imageViewDelete.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            imageViewAddPasswordInCategory.setVisibility(View.VISIBLE);
            imageViewAddPasswordInCategory.setOnClickListener(this);
        } else {
            imageViewDown.setVisibility(View.VISIBLE);
            imageViewUp.setVisibility(View.VISIBLE);
            imageViewDelete.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            imageViewAddPasswordInCategory.setVisibility(View.GONE);
            int childIcon=child.getIcon();
            if (childIcon>0){
                Glide.with(convertView.getContext()).load(childIcon).into(imageView);
            }
            linearLayoutChild.setOnClickListener(this);

        }

        txtListChild.setText(child.getTitle());


        imageViewUp.setOnClickListener(this);
        imageViewDown.setOnClickListener(this);
        imageViewDelete.setOnClickListener(this);

        ViewHelper.addHover(imageViewAddPasswordInCategory);
        ViewHelper.addHover(linearLayoutChild);
        ViewHelper.addHover(imageViewUp);
        ViewHelper.addHover(imageViewDown);
        ViewHelper.addHover(imageViewDelete);

        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        PasswordCategory passwordCategory = (PasswordCategory) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exapandible_password_list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);

        try {
            List<Passwords> childCount=db.getAllPasswordsForCategory(passwordCategory);

            lblListHeader.setText(passwordCategory.getName()+" ( "+childCount.size()+" )");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ImageView imageView= (ImageView) convertView.findViewById(R.id.imageView);

        Glide.with(convertView.getContext()).load(passwordCategory.getIcon().intValue()).into(imageView);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onClick(View v) {
     EventBus.getDefault().post(new OnPasswordListOptionsClicked(v));
    }
}
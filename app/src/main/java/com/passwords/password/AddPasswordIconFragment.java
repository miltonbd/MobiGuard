package com.passwords.password;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.fs.lib.util.MyLogger;
import com.mobiguard.R;
import com.passwords.category.CategoryIconAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
public class AddPasswordIconFragment extends DialogFragment {
    List<Integer> myDataset = new ArrayList<Integer>();
    @Bind(R.id.buttonCancel)
    Button buttonCancel;

    @Bind(R.id.buttonOk)
    Button buttonOk;

    private CategoryIconAdapter mAdapter;

    @Bind(R.id.my_recycler_view)
    GridView mRecyclerView;

    public AddPasswordIconFragment() {
    }

    public static AddPasswordIconFragment newInstance() {
        AddPasswordIconFragment fragment = new AddPasswordIconFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment3
        View view = inflater.inflate(R.layout.fragment_add_password_category_icon, container, false);
        getDialog().setTitle("Select Password Icon");
        ButterKnife.bind(this, view);

        myDataset.add(R.drawable.ic_menu_gallery);
        myDataset.add(R.drawable.ic_credit_card);
        myDataset.add(R.drawable.ic_email);
        myDataset.add(R.drawable.ic_internet);
        myDataset.add(R.drawable.ic_menu_camera);
        myDataset.add(R.drawable.ic_menu_manage);
        myDataset.add(R.drawable.ic_menu_share);
        myDataset.add(R.drawable.ic_menu_slideshow);
        myDataset.add(R.drawable.ic_website);
        myDataset.add(R.drawable.icon_bar_chart);
        myDataset.add(R.drawable.icon_book);
        myDataset.add(R.drawable.icon_chart);
        myDataset.add(R.drawable.icon_dollar);
        myDataset.add(R.drawable.icon_dustbin);
        myDataset.add(R.drawable.icon_key);
        myDataset.add(R.drawable.icon_list_user);
        myDataset.add(R.drawable.icon_question);
        myDataset.add(R.drawable.icon_setting);
        myDataset.add(R.drawable.icon_shopping_cart);
        myDataset.add(R.drawable.icon_star);
        myDataset.add(R.drawable.icon_time_calendar);
        myDataset.add(R.drawable.icon_unpack);
        myDataset.add(R.drawable.icon_world);

        MyLogger.debug("Password category count " + myDataset.size());
        mAdapter = new CategoryIconAdapter(getContext(),R.layout.list_item_password_category_icon,myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @OnItemClick(R.id.my_recycler_view)
    public void onIconClicked(int position) {
        // get the icon and send the icon id
        Integer icon= myDataset.get(position);
        EventBus.getDefault().post(new OnAddPasswordIconSelected(icon));
        getDialog().cancel();
    }


    @OnClick(R.id.buttonCancel)
    public void onCancel() {
        getDialog().cancel();
    }

    @OnClick(R.id.buttonOk)
    public void onOk() {

    }

}

package com.passwords.category;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fs.lib.util.OnConfirmDialog;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnAttachFragment;
import com.passwords.events.OnRefreshPasswordCategory;
import com.passwords.password.OnRefreshPasswords;
import com.util.MyDatabaseHelper;
import com.fs.lib.util.MyLogger;
import com.mobiguard.MyApp;
import com.mobiguard.R;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PasswordCategoryFragment extends Fragment implements PasswordCategoryAdapter.MyClickListener {
    private MyDatabaseHelper db;
    private PasswordCategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentManager fragmentManager;

    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.menu_action_add)
    ImageView menu_action_add;

    private List<PasswordCategory> items = new ArrayList<>();

    public PasswordCategoryFragment() {
        setHasOptionsMenu(true);
    }

    public void onEvent(OnRefreshPasswordCategory event) {
        try {
            Dao<PasswordCategory,Long> passwordCategories= db.getPasswordCategoryDao();
             items = passwordCategories.queryForAll();
            MyLogger.debug("Password category count " + items.size());
            mAdapter = new PasswordCategoryAdapter(items);
            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_category, null);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        loadItems();
        fragmentManager = getActivity().getSupportFragmentManager();
        return view;
    }

    private void loadItems() {
        EventBus.getDefault().post(new OnRefreshPasswords());
        try {
          Dao<PasswordCategory,Long> passwordCategories= db.getPasswordCategoryDao();
           QueryBuilder<PasswordCategory, Long> cb= passwordCategories.queryBuilder();
            cb.orderBy(MyDatabaseHelper.order,true);
            items = passwordCategories.query(cb.prepare());
          MyLogger.debug("Password category count " + items.size());
          mAdapter = new PasswordCategoryAdapter(items);
            mAdapter.setOnItemClickListener(this);
          mRecyclerView.setAdapter(mAdapter);

      } catch (Exception e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
      }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new OnActionBarTitleChange("Password Category"));
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.menu_action_add)
    public void onMenuACtionAddClicked() {
        AddPasswordCategoryFragment fragment = AddPasswordCategoryFragment.newInstance(null);
        EventBus.getDefault().post(new OnAttachFragment(fragment));
    }

    @Override
    public void onItemClick(int position, View v) {
        // get PasswordCategory
        final PasswordCategory selectedItem= items.get(position);
            try {
                if (v.getId()==R.id.imageViewDelete) {

                    new OnConfirmDialog(getContext(),"Are You Sure to delete?") {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        db.deletePasswordCategory(selectedItem);
                                        loadItems();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    }.show();
                    loadItems();
                    MyLogger.debug("Order Up");
                    return;
                }
                if (v.getId()==R.id.imageViewUp) {
                    db.orderPasswordCategoryUp(selectedItem);
                    loadItems();
                    MyLogger.debug("Order Up");
                    EventBus.getDefault().post(new OnRefreshPasswords());
                    return;
                }
                if (v.getId()==R.id.imageViewDown) {
                    db.orderPasswordCategoryDown(selectedItem);
                    MyLogger.debug("Order Down");
                    loadItems();
                    EventBus.getDefault().post(new OnRefreshPasswords());
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        PasswordCategory passwordCategory= items.get(position);
        assert passwordCategory!=null;
        MyLogger.debug("PasswordCategory clicked "+passwordCategory.toString());
        // Show
        AddPasswordCategoryFragment addPasswordCategoryFragment = AddPasswordCategoryFragment.newInstance(passwordCategory);

        // attach AddPasswordCategoryFragment for edit.
        EventBus.getDefault().post(new OnAttachPasswordEditFragment(addPasswordCategoryFragment));
    }
}

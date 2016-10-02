package com.passwords.password;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.fs.lib.util.MyLogger;
import com.fs.lib.util.OnConfirmDialog;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnAttachFragment;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.passwords.category.PasswordCategory;
import com.passwords.fieldvalues.PasswordFields;
import com.util.MyDatabaseHelper;
import com.util.Statics;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PasswordsFragment extends Fragment {
    private MyDatabaseHelper db;
    public final static String addPaswordDialog = "AddPaswordDialog";
    @Bind(R.id.passwordsList)
    ExpandableListView expListView;

    @Bind(R.id.menu_action_add)
    ImageView menu_action_add;

    PasswordExpandableListAdapter listAdapter;
    List<PasswordCategory> listDataHeader;
    HashMap<PasswordCategory, List<Passwords>> listDataChild;

    public PasswordsFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_list, null);
        ButterKnife.bind(this, view);
        db = MyDatabaseHelper.getInstance(getContext());
        final FragmentManager fragmentManager =  getActivity().getSupportFragmentManager();
        loadItems();

        return view;
    }

    @OnClick(R.id.menu_action_add)
    public void onClickAdd(View view) {
        AddPasswordFragment fragment = AddPasswordFragment.newInstanceForAdd();
        EventBus.getDefault().post(new OnAttachFragment(fragment));
    }

    private void loadItems() {
        prepareListData();
        listAdapter = new PasswordExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    public void onEvent(OnRefreshPasswords event) {
        loadItems();
    }
    public void onEvent(OnPasswordListOptionsClicked event) {
        final View v=event.getView();
        final Passwords password= (Passwords) v.getTag();
        MyLogger.debug("Password from OnPasswordListOptionsClicked event "+password);
        try {
        switch(v.getId()){
            case R.id.imageViewAddPasswordInCategory:
                MyLogger.debug("explist up "+password);
                AddPasswordFragment fragment1 = AddPasswordFragment.newInstanceForAddWithPasswordCategory(password.getPasswordCategory());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment1).
                        addToBackStack(Statics.FRAGMENT_TAG_ADD_PASSWORD).commit();
                loadItems();
                break;

            case R.id.linearLayoutChild:
                MyLogger.debug("explist up "+password);
                AddPasswordFragment fragment = AddPasswordFragment.newInstanceForEdit(password);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).
                        addToBackStack(Statics.FRAGMENT_TAG_ADD_PASSWORD).commit();
                loadItems();
                break;
            case R.id.imageViewUp:
                MyLogger.debug("explist up "+password);
                try {
                    db.orderPasswordyUp(password);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                loadItems();
                break;

            case R.id.imageViewDown:
                MyLogger.debug("explist Down "+password);
                db.orderPasswordDown(password);
                loadItems();
                break;

            case R.id.imageViewDelete:
                MyLogger.debug("explist Delete "+password);
                new OnConfirmDialog(v.getContext(),"Are you sure you want to Delete?") {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                try {
                                    db.deletePassword(password);
                                    loadItems();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                        expListView.expandGroup(password.getGroupPosition());
                    }
                }.show();
                break;

        };
        } catch (SQLException e) {
            e.printStackTrace();
        }

        expListView.expandGroup(password.getGroupPosition());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnActionBarTitleChange("Password List"));
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<PasswordCategory>();
        listDataChild = new HashMap<PasswordCategory, List<Passwords>>();

        try {
            List<PasswordCategory> categories = db.getAllPasswordCategory();
            Dao<PasswordFields, Long> passwordCategoryFieldsDao= db.getPasswordFieldsDao();
            int i=0;
            for(PasswordCategory category :categories) {
                listDataHeader.add(category);
                // Adding child data
               //String  passwordCategoryFieldsDao.queryBuilder().where().eq("category_id", category).query();

                List<Passwords> categoryFields=  db.getAllPasswordsForCategory(category);
                if (categoryFields.size()==0) {
                    Passwords tmpPassword=new Passwords();
                    tmpPassword.setTitle("No item");
                    tmpPassword.setIcon(R.drawable.icon_unpack);
                    tmpPassword.setPasswordCategory(category);
                    categoryFields.add(tmpPassword);
                }
                listDataChild.put(listDataHeader.get(i), categoryFields); // Header, Child data
                i++;
            }

        } catch (SQLException e) {
            MyLogger.debug( e.getMessage());
        }
    }
}
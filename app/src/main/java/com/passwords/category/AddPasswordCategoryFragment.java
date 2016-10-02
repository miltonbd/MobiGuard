package com.passwords.category;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.ViewHelper;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.R;
import com.passwords.events.OnAddCategoryIconSelected;
import com.passwords.events.OnBackPressed;
import com.passwords.events.OnRefreshPasswordCategory;
import com.passwords.fieldvalues.PasswordFields;
import com.passwords.password.OnRefreshPasswords;
import com.util.MyDatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AddPasswordCategoryFragment extends BaseFragment implements AddPasswordCategoryFieldAdapter.MyClickListener {

    @Bind(R.id.buttonAddField)
    Button buttonAddField;

    @Bind(R.id.editTextCategory)
    EditText editTextCategory;

    @Bind(R.id.editTextFieldName)
    EditText editTextFieldName;

    @Bind(R.id.iconCategory)
    ImageView iconCategory;

    @Bind(R.id.passCategoryFieldRecy)
    RecyclerView passCategoryFieldRecy;

    @Bind(R.id.menu_action_save)
    ImageView menu_action_save;

    private AddPasswordCategoryFieldAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PasswordFields> categoryFields = new ArrayList<PasswordFields>();
    private static PasswordCategory passwordCategory = new PasswordCategory();
    private static boolean isAdd;
    private MyDatabaseHelper db;
    private Integer icon=R.drawable.icon_star;

    public AddPasswordCategoryFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void backPressedInFragment() {

    }

    public static AddPasswordCategoryFragment newInstance(PasswordCategory passwordCategory1) {
        AddPasswordCategoryFragment fragment = new AddPasswordCategoryFragment();
        passwordCategory = passwordCategory1;
        if (passwordCategory == null) {
            isAdd = true;
            passwordCategory = new PasswordCategory();
            fragment.setPasswordCategory(passwordCategory);
        } else {
            isAdd = false;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onEvent(OnAddCategoryIconSelected event) {
        iconCategory.setImageResource(event.getIcon());
        this.icon=event.getIcon();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment3
        View view = inflater.inflate(R.layout.fragment_add_password_category, container, false);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        db = MyDatabaseHelper.getInstance(getContext());
        passCategoryFieldRecy.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        passCategoryFieldRecy.setLayoutManager(mLayoutManager);
        MyLogger.debug("Password category count " + categoryFields.size());
        if (isAdd) {
            EventBus.getDefault().post(new OnActionBarTitleChange("New Password Category"));
        } else {
            EventBus.getDefault().post(new OnActionBarTitleChange("Edit Password Category"));
            /**
             * If PasswordCategory is present then populate the fields with Fields for that category.
             */
            setPasswordCategory(passwordCategory);
            iconCategory.setImageResource(passwordCategory.getIcon().intValue());
            // getDialog().setTitle("Edit Password Category");
            try { // load Category fields for category
                categoryFields = db.getAllFieldsForCategory(passwordCategory);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadCategoryFields();
        }
        return view;
    }

    @OnClick(R.id.menu_action_save)
    public void onActionMenuSave(View view){
        onOk();
    }

    public PasswordCategory getPasswordCategory() {
        return passwordCategory;
    }

    public void setPasswordCategory(PasswordCategory passwordCategory) {
        this.passwordCategory = passwordCategory;
        if (passwordCategory != null && passwordCategory.getId() != null) {
            this.editTextCategory.setText(passwordCategory.getName());
        }
    }

    @OnClick(R.id.buttonAddField)
    public void onAddField() {
        String fieldName = editTextFieldName.getText().toString();
        if (fieldName.matches("")) {
            MyLogger.show(getContext(), "Category Field Can not be empty.");
            return;
        }
        PasswordFields passwordCategoryFields = new PasswordFields();
        passwordCategoryFields.setName(fieldName);
        this.categoryFields.add(passwordCategoryFields);
        loadCategoryFields();
    }

    private void loadCategoryFields() {
        mAdapter = new AddPasswordCategoryFieldAdapter(categoryFields);
        passCategoryFieldRecy.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        editTextFieldName.setText("");
    }

    @OnClick(R.id.iconCategory)
    public void onIconCategoryClicked() {
        FragmentManager fg = getActivity().getSupportFragmentManager();
        AddPasswordCategoryIconFragment iconFragment = AddPasswordCategoryIconFragment.newInstance();
        iconFragment.show(fg, "iconChooserDialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.save_cancel, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                break;
            case R.id.action_cancel:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onOk() {
        // Fields for password Category can not be empty.
        if (categoryFields.size() == 0) {
            MyLogger.show(getContext(), "Category Fields can not be empty.");
            return;
        }
        String categoryName = editTextCategory.getText().toString();
        if (categoryName.matches("")) {
            MyLogger.show(getContext(), "Category Name can not be empty.");
            return;
        }
        if (categoryFields.size()==0) {
            MyLogger.show(getContext(), "Password Category Fields can not be empty.");
            return;
        }

        try {
            if (isAdd) {
                MyLogger.debug("Adding Password Category. ");
                PasswordCategory passwordCategory2=new PasswordCategory();
                passwordCategory2.setName(categoryName);
                passwordCategory2.setIcon(icon.longValue());
                PasswordCategory pas1= db.createPasswordCategory(passwordCategory2);
                MyLogger.debug("New Password Category Created Successfully. "+pas1);
                db.createFieldForPasswordCategory(pas1, categoryFields);
                MyLogger.show(getContext(), "Password Category Added Successfully.");
            } else {
                MyLogger.debug("Editing Password Category. ");
                passwordCategory.setName(categoryName);
                passwordCategory.setIcon(icon.longValue());
                PasswordCategory pas1= db.createPasswordCategory(passwordCategory);
                db.createFieldForPasswordCategory(pas1, categoryFields);
                MyLogger.show(getContext(), "Password Category Edited Successfully.");
            }
            // Add to CategoryFieldTable

            EventBus.getDefault().post(new OnRefreshPasswordCategory());
            EventBus.getDefault().post(new OnBackPressed());
        } catch (SQLException e) {
            MyLogger.debug("onOk "+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position, View v) {
        MyLogger.debug("Edit Password Field clicked.");
        // get PasswordCategory
        final PasswordFields selectedItem= categoryFields.get(position);
        RelativeLayout parentView=null;
        String fieldName="";
                MyLogger.debug("Selected item in onItemClick " + selectedItem);
        int j=0;
        switch (v.getId()) {
            case R.id.imageViewDelete:
                // remove the
                int i=0;
                for (PasswordFields passwordCategory:this.categoryFields) {
                    if (passwordCategory.getName().matches(selectedItem.getName())) {
                        this.categoryFields.remove(i);
                        try {
                            db.deletePasswordFields(passwordCategory);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    i++;
                }
                loadCategoryFields();
                break;

            case R.id.imageViewUp:

                for (PasswordFields passwordCategory:this.categoryFields) {
                    if (passwordCategory.getName().matches(selectedItem.getName())&&j>0) {
                        //Select Previous one and current one.
                        int k=j-1;
                        PasswordFields KiTEM= categoryFields.get(k);
                        PasswordFields jiTEM= categoryFields.get(j);
                        categoryFields.set(k,jiTEM);
                        categoryFields.set(j,KiTEM);
                        break;
                    }
                    j++;
                }
                loadCategoryFields();
                MyLogger.debug("Order Up in Add PasswordCategory");
                EventBus.getDefault().post(new OnRefreshPasswords());
                break;
            case R.id.imageViewDown:
                MyLogger.debug("Order Down in Add PasswordCategory");
                 j=0;
                for (PasswordFields passwordCategory:this.categoryFields) {
                    if (passwordCategory.getName().matches(selectedItem.getName())&&j<categoryFields.size()-1) {
                        //Select Previous one and current one.
                        int k=j+1;
                        PasswordFields KiTEM= categoryFields.get(k);
                        PasswordFields jiTEM= categoryFields.get(j);
                        categoryFields.set(k,jiTEM);
                        categoryFields.set(j,KiTEM);

                        break;
                    }
                    j++;
                }
                loadCategoryFields();
                EventBus.getDefault().post(new OnRefreshPasswords());
                break;
            case R.id.imageViewEdit:
                parentView= (RelativeLayout) v.getParent();
                fieldName=  ViewHelper.toggleTextViewEditText(parentView, R.id.textViewFieldName, R.id.editTextFieldName);
                MyLogger.debug("imageViewEdit in Add PasswordCategory");
                break;

            case R.id.textViewFieldName:
                 parentView= (RelativeLayout) v.getParent();
                 fieldName=  ViewHelper.toggleTextViewEditText(parentView, R.id.textViewFieldName, R.id.editTextFieldName);

                MyLogger.debug("imageViewEdit in Add PasswordCategory");
                break;

        }

        // Show
        //AddPasswordCategoryFragment addPasswordCategoryFragment = AddPasswordCategoryFragment.newInstance(passwordCategory);

        // attach AddPasswordCategoryFragment for edit.
        //EventBus.getDefault().post(new OnAttachPasswordEditFragment(addPasswordCategoryFragment));
    }

    @Override
    public void onOnFocusChanged(int position, View v, boolean hasFocus) {
        RelativeLayout parent= (RelativeLayout) v.getParent();
        switch (v.getId()){
            case R.id.editTextFieldName:
                if (hasFocus){

                }else {
                    MyLogger.debug("editTextFieldName is out of focus. ");
                   String s= ViewHelper.toggleTextViewEditText(parent, R.id.textViewFieldName, R.id.editTextFieldName);
                    categoryFields.get(position).setName(s);
               }
                break;
        }

    }


    // Todo OnBackPressed show dialog to confirm.

}

package com.passwords.password;


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
import android.widget.Spinner;

import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyLogger;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.passwords.category.PasswordCategory;
import com.passwords.events.OnBackPressed;
import com.passwords.events.OnRefreshPasswordCategory;
import com.passwords.fieldvalues.CategoryFieldValuesAdapter;
import com.passwords.fieldvalues.PasswordFields;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import de.greenrobot.event.EventBus;

public class AddPasswordFragment extends BaseFragment implements CategoryFieldValuesAdapter.MyClickListener {

    @Bind(R.id.buttonAddField)
    public Button buttonAddField;

    @Bind(R.id.spinnerCategory)
    public Spinner spinnerCategory;

    @Bind(R.id.editTextFieldName)
    public EditText editTextFieldName;

    @Bind(R.id.editTexTTitle)
    public EditText editTexTTitle;

    @Bind(R.id.iconCategory)
    public ImageView iconCategory;


    @Bind(R.id.menu_action_save)
    public ImageView menu_action_save;

    @Bind(R.id.passCategoryFieldRecy)
    RecyclerView passCategoryFieldRecy;
    private MyDatabaseHelper db;
    private CategoryFieldValuesAdapter categoryFieldValuesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PasswordFields> passswordFieldsCustom = new ArrayList<PasswordFields>();
    private List<PasswordFields> passwordFieldsList = new ArrayList<PasswordFields>();
    private List<PasswordFields> totalFields=  new ArrayList<PasswordFields>();
    private List<PasswordCategory> passwordCategories=new ArrayList<>();
    private AddPasswordSelectCategoryAdapter spinnerAdapter;
    private Passwords currentPassword=new Passwords();
    private Integer icon= R.drawable.ic_website;
    private static boolean isAdd=false;

    public AddPasswordFragment() {
            setHasOptionsMenu(true);
    }

    /**
     * if password is null, then isAdd is true else isAdd is false meaning editing the item.
     * @return
     */
    public static AddPasswordFragment newInstanceForAdd() {
        AddPasswordFragment fragment = new AddPasswordFragment();
        fragment.setCurrentPassword(new Passwords());
        isAdd=true;
        return fragment;
    }
    public static AddPasswordFragment newInstanceForEdit(Passwords password1) {
        AddPasswordFragment fragment = new AddPasswordFragment();
        fragment.setCurrentPassword(password1);
        isAdd=false;
        return fragment;
    }
    public static AddPasswordFragment newInstanceForAddWithPasswordCategory(PasswordCategory passwordCategory) {
        AddPasswordFragment fragment = new AddPasswordFragment();
        Passwords password1=new Passwords();
        password1.setPasswordCategory(passwordCategory);
        fragment.setCurrentPassword(password1);
        isAdd=true;
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_password, container, false);
        ButterKnife.bind(this, view);
        init(view);
        try {
            initSpinner();
            if (isAdd) {
                EventBus.getDefault().post(new OnActionBarTitleChange("Add Password"));
                MyLogger.debug("Adding password  " + currentPassword);
               if (currentPassword.getPasswordCategory()==null){
                   currentPassword.setPasswordCategory(passwordCategories.get(0));
               }
                passwordFieldsList = db.getAllFieldsForCategory(currentPassword.getPasswordCategory());
            } else {
                icon=currentPassword.getIcon();
                MyLogger.debug("Editing Current Password " + currentPassword);
                EventBus.getDefault().post(new OnActionBarTitleChange("Edit Password"));
                editTexTTitle.setText(currentPassword.getTitle());
                iconCategory.setImageResource(currentPassword.getIcon());
                passwordFieldsList = db.getAllFieldsAndValuesForCategoryInEditPassword(currentPassword);
                MyLogger.debug("Password has Fields  "+passwordFieldsList);
            }
            setFieldValuesAdapter();
        }
        catch (SQLException e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);
        return view;
    }

    private void initSpinner() throws SQLException {
        passwordCategories = db.getAllPasswordCategory();
        spinnerAdapter = new AddPasswordSelectCategoryAdapter
                (getContext(), R.layout.list_item_select_password_category, passwordCategories);
        spinnerCategory.setAdapter(spinnerAdapter);
        int index=0;
        for (PasswordCategory item :passwordCategories) {
            if (currentPassword!=null) {
                PasswordCategory passwordCategory = currentPassword.getPasswordCategory();
                if (passwordCategory !=null&&item.getName().matches(passwordCategory.getName())) {
                    spinnerCategory.setSelection(index);
                    break;
                }
                index++;
            }
        }
    }

    private void init(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        passCategoryFieldRecy.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        passCategoryFieldRecy.setLayoutManager(mLayoutManager);
        db = MyDatabaseHelper.getInstance(getContext());
    }

    private void setFieldValuesAdapter() {
        setFieldValues();
        MyLogger.debug("Total Fields " + totalFields.size());
        categoryFieldValuesAdapter = new CategoryFieldValuesAdapter(totalFields);
        categoryFieldValuesAdapter.setOnItemClickListener(this);
        passCategoryFieldRecy.setAdapter(categoryFieldValuesAdapter);
    }

    private void setFieldValues() {
        totalFields=new ArrayList<>();
        totalFields.addAll(passwordFieldsList);
        totalFields.addAll(passswordFieldsCustom);
    }

    @OnClick(R.id.menu_action_save)
    public void onMenuActionSaveClicked(View view) {
        onOk();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // inflater.inflate(R.menu.save_cancel, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onEvent(OnAddPasswordIconSelected event) {
        MyLogger.debug("icon id " + event.getIcon());
        icon = event.getIcon();
        iconCategory.setImageResource(icon);
    }

    @OnClick(R.id.buttonAddField)
    public void onAddField() {
        /**
         * each password can have some custom fields which is not under category. retrieving password from
         * PasswordsFieldsValues we should use password_id
          */
        String fieldName= editTextFieldName.getText().toString();
        PasswordFields passwordCategoryFields = new PasswordFields();
        passwordCategoryFields.setName(fieldName);
        passwordCategoryFields.setIsCustom(true);
        passswordFieldsCustom.add(passwordCategoryFields);
        setFieldValuesAdapter();
    }

    public Passwords getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(Passwords currentPassword) {
        this.currentPassword = currentPassword;
    }


    /**
     * populate the field list with total fields=fields for category + custom passsword fields
     * @param position
     */
    @OnItemSelected(R.id.spinnerCategory)
    public void onItemSelected(int position) {
        PasswordCategory selectedPasswordCategory = passwordCategories.get(position);
        MyLogger.debug("Spinner selected Password Category " + selectedPasswordCategory);
        try {
            if(isAdd){
                passwordFieldsList = db.getAllFieldsForCategory(selectedPasswordCategory);
            }else {
                passwordFieldsList = db.getAllFieldsAndValuesForCategoryInEditPassword(currentPassword);
            }
            currentPassword.setPasswordCategory(selectedPasswordCategory);
            setFieldValuesAdapter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iconCategory)
    public void onIconCategoryClicked()
    {
        FragmentManager fg= getActivity().getSupportFragmentManager();
        AddPasswordIconFragment iconFragment = AddPasswordIconFragment.newInstance();
        iconFragment.show(fg, "iconCategory");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                onOk();
                break;
            case R.id.action_cancel:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onOk() {
        PasswordCategory selectedPasswordCategory= passwordCategories.get(spinnerCategory.getSelectedItemPosition());
        try {
            Dao<Passwords,Long> passwordsDao= db.getPasswordsDao();
            String textTitle=editTexTTitle.getText().toString();
            if (textTitle.matches("")){
                MyLogger.show(getContext(),"Password Title needed.");
                return;
            }
            currentPassword.setTitle(textTitle);
            currentPassword.setIcon(icon);
            currentPassword.setPasswordCategory(selectedPasswordCategory);
            currentPassword = db.createPassword(currentPassword);
            createCustomFields();
            // Add to CategoryFieldTableDao<PasswordFields, Long> categoryFieldDao= db.getPasswordFieldsDao();
            setFieldValues();
            //category
            int i=0;
            MyLogger.debug("Creating Field Values for "+totalFields.size());
            for (PasswordFields field :totalFields) {
                CategoryFieldValuesAdapter.DataObjectHolder vh= (CategoryFieldValuesAdapter.DataObjectHolder) passCategoryFieldRecy.findViewHolderForAdapterPosition(i);
                if (vh!=null) {
                    String value=vh.editTextFieldValue.getText().toString();
                    db.createFieldValue(currentPassword, field, selectedPasswordCategory, value);
                }
                i++;
            }
            //db.createFieldValue(currentPassword,selectedPasswordCategory,);
            if (isAdd) {
                MyLogger.show(getContext(), "New Password created Successfully.");
            }else {
                MyLogger.show(getContext(), "Password Edited Successfully.");
            }
            EventBus.getDefault().post(new OnRefreshPasswordCategory());
            EventBus.getDefault().post(new OnBackPressed());
        } catch (SQLException e) {
            MyLogger.debug("from onOk create password "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void createCustomFields() throws SQLException {
        passswordFieldsCustom=db.createCustomFieldForPassword(currentPassword,passswordFieldsCustom);
        MyLogger.debug("Custom fields are "+passswordFieldsCustom);
    }


    @Override
    public void onItemClick(int position, View v) {
        MyLogger.debug("Itm clicked in "+getClass().getName());
    }
    @Override
    public void backPressedInFragment() {

    }
}

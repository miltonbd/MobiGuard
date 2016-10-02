package com.passwords.category;

/**
 * Created by milton on 27/01/16.
 */
public class OnAttachPasswordEditFragment {
    AddPasswordCategoryFragment addPasswordCategoryFragment;

    public AddPasswordCategoryFragment getAddPasswordCategoryFragment() {
        return addPasswordCategoryFragment;
    }

    public OnAttachPasswordEditFragment(AddPasswordCategoryFragment addPasswordCategoryFragment) {
        this.addPasswordCategoryFragment = addPasswordCategoryFragment;
    }
}

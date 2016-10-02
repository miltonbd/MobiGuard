package com.mobiguard;


import com.fs.lib.util.BaseFragment;

/**
 * Created by milton on 11/02/16.
 */
public class OnRemoveFragmentFromBackStack {
    private BaseFragment fragment;

    public OnRemoveFragmentFromBackStack(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }
}

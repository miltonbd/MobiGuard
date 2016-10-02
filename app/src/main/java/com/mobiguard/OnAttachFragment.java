package com.mobiguard;


import com.fs.lib.util.BaseFragment;

/**
 * Created by milton on 12/02/16.
 */
public class OnAttachFragment {
    private BaseFragment fragment;

    public OnAttachFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }
}

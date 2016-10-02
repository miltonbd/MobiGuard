package com.mobiguard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applock.ListAppLockFragment;
import com.callsms.BlockedCallSMSContainerFragment;
import com.images.ListLockedImageFileFragment;
import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyLogger;
import com.mobiguard.R;
import com.passwords.PasswordContainerFragment;
import com.videos.ListLockedVideoFileFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class DashboardFragment extends BaseFragment {
    @Bind(R.id.passwordDashboard)
    View passwordDashboard;

    @Bind(R.id.videosDashboard)
    View videosDashboard;

    @Bind(R.id.picturesDashboard)
    View picturesDashboard;

    @Bind(R.id.appLockDashboard)
    View appLockDashboard;

    public DashboardFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnCheckDrawerPosition(0));
        EventBus.getDefault().post(new OnActionBarTitleChange("Dashboard"));
        MyLogger.debug("DashboardFragment onResume");
    }

    @OnClick(R.id.passwordDashboard)
    public void onPasswordDashboadClicked(){
       PasswordContainerFragment passwordFragment=new PasswordContainerFragment();
        EventBus.getDefault().post(new OnAttachFragment(passwordFragment));
        EventBus.getDefault().post(new OnCheckDrawerPosition(1));
    }

    @OnClick(R.id.videosDashboard)
    public void onVideosDashboardClicked(){
        ListLockedVideoFileFragment passwordFragment= new ListLockedVideoFileFragment();
        EventBus.getDefault().post(new OnAttachFragment(passwordFragment));
        EventBus.getDefault().post(new OnCheckDrawerPosition(3));
    }


    @OnClick(R.id.picturesDashboard)
    public void onpicturesDashboardClicked(){
        ListLockedImageFileFragment picturespLockFragment=new ListLockedImageFileFragment();
        EventBus.getDefault().post(new OnAttachFragment(picturespLockFragment));

        EventBus.getDefault().post(new OnCheckDrawerPosition(2));
    }

    @OnClick(R.id.appLockDashboard)
    public void onAppLockDashboardClicked(){
        ListAppLockFragment appLockFragment=new ListAppLockFragment();
        EventBus.getDefault().post(new OnAttachFragment(appLockFragment));
        EventBus.getDefault().post(new OnCheckDrawerPosition(4));
    }

    @OnClick(R.id.callSmsBlockDashboard)
    public void onCallSmsBlockDashboardClicked(){
        BlockedCallSMSContainerFragment fragment=new BlockedCallSMSContainerFragment();
        EventBus.getDefault().post(new OnAttachFragment(fragment));
        EventBus.getDefault().post(new OnCheckDrawerPosition(5));
    }

    @OnClick(R.id.spyDashboard)
    public void onSpyDashboardClicked(){
      //  SpyContainerFragment fragment= new SpyContainerFragment();

        //EventBus.getDefault().post(new OnAttachFragment(fragment));
        rateApp();
        EventBus.getDefault().post(new OnCheckDrawerPosition(6));
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
}

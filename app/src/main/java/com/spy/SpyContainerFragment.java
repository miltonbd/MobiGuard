package com.spy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fs.lib.util.BaseFragment;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;

import de.greenrobot.event.EventBus;

public class SpyContainerFragment extends BaseFragment {

    private SpyContainerFragmentAdapter callSMSContainerFragmentAdapter;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public String[] titles={"Recorded Call List","Recorded Video List","Recorded Audio List"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Recorded Call"));
        tabLayout.addTab(tabLayout.newTab().setText("Recorded Video"));
        tabLayout.addTab(tabLayout.newTab().setText("Recorded Audio"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        callSMSContainerFragmentAdapter = new SpyContainerFragmentAdapter(getActivity().getSupportFragmentManager(),3);
        viewPager.setAdapter(callSMSContainerFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                EventBus.getDefault().post(new OnActionBarTitleChange(titles[tab.getPosition()]));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return x;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnActionBarTitleChange(titles[0]));
//        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
    }

    @Override
    public void backPressedInFragment() {
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(true));
    }
}

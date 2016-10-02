package com.spy.audiorecorder;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.callsms.calls.BlockedCalls;
import com.fs.lib.util.BaseAlertDialog;
import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.OnConfirmDialog;
import com.mobiguard.MyApp;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AudioRecordFragment extends BaseFragment implements MyEventListener {
    @Bind(R.id.actionbar_selected_items_count)
    TextView actionbar_selected_items_count;

    @Bind(R.id.textViewSelectedTitle)
    TextView textViewSelectedTitle;

    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.textViewEmpty)
    TextView textViewEmpty;

    @Bind(R.id.menu_action_save)
    ImageView menu_action_save;

    @Bind(R.id.menu_action_clear)
    ImageView menu_action_clear;

    @Bind(R.id.bottomMenu)
    Toolbar bottomMenu;

    private List<AudioRecordPojo> items = new ArrayList<>();
    private MyDatabaseHelper db;
    private AudioRecordAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentManager fragmentManager;
    private BaseAlertDialog onNewNumberAlertDialog;
    private BaseAlertDialog onItemClickedAlertDialog;
    private AudioRecordPojo audioRecordPojo;
    private boolean isCabActive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spy_audio_record_list, null);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        loadItems();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
        makeBottomMenuAdd();
    }

    private void loadItems() {
        try {
            items = db.getAllAudioRecords();
            if (items.size() > 0) {
                textViewEmpty.setVisibility(View.GONE);
            }else {
                textViewEmpty.setVisibility(View.VISIBLE);
            }
            MyLogger.debug("Call logs count " + items.size());
            mAdapter = new AudioRecordAdapter(items, R.layout.list_item_black_listed_numbers);
            mAdapter.setMyEventListener(this);
            mRecyclerView.setAdapter(mAdapter);

        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteNumber(AudioRecordPojo m) {
        try {
            DeleteBuilder<BlockedCalls, Long> dao=  db.getBlockedCallDao().deleteBuilder();
            dao.where().eq(MyDatabaseHelper.id, m.getId());
            dao.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void makeBottomMenuAdd(){
        actionbar_selected_items_count.setVisibility(View.GONE);
        menu_action_clear.setVisibility(View.GONE);
        textViewSelectedTitle.setVisibility(View.GONE);
        menu_action_save.setImageResource(R.drawable.ic_add_white_48dp);
        textViewSelectedTitle.setVisibility(View.GONE);
    }

    private void makeBottomMenuMultipleSelect(){
        actionbar_selected_items_count.setVisibility(View.VISIBLE);
        menu_action_clear.setVisibility(View.VISIBLE);
        textViewSelectedTitle.setVisibility(View.VISIBLE);
        menu_action_save.setImageResource(R.drawable.ic_cab_done_holo_dark);
        textViewSelectedTitle.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.menu_action_save)
    public void OnMenuAddActionClicked(View view) {
        if (!isCabActive) {
            AddAudioRedcordDialogFragment dialogFragment=AddAudioRedcordDialogFragment.newInstanceForAdd();
            dialogFragment.show(getActivity().getSupportFragmentManager(),"AddAudioRecordDiaalog");
        } else {
            MyLogger.debug("Saving Add Image to Vault");
            OnConfirmDialog dialog=   new OnConfirmDialog(getContext(),"Do you want to delete?"){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            for ( Map.Entry<Integer, Boolean> item :mAdapter.selectedItems.entrySet()) {
                                AudioRecordPojo m= mAdapter.items.get(item.getKey());
                                deleteNumber(m);
                            }
                            mAdapter.clearSelections();
                            bottomMenu.setVisibility(View.GONE);
                            EventBus.getDefault().post(new OnActionBarTitleChange("Blocked Calls"));
                            isCabActive=false;
                            loadItems();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.cancel();
                            break;
                    }
                }
            };
            dialog.show();
        }
    }

    @Override
    public void backPressedInFragment() {

    }

    @Override
    public void onItemClick(int position, View v) {
        if (!isCabActive) { //add
            MyLogger.debug("onItemClick in  " + getClass().getName());
            audioRecordPojo = mAdapter.items.get(position);

            // show Alert Dialog
        } else {
            AudioRecordPojo item = mAdapter.items.get(position);
            mAdapter.toggleSelection(position);
            actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
            if (mAdapter.selectedItems.size() == 0) {
                EventBus.getDefault().post(new OnActionBarTitleChange("Blocked Number List"));
                isCabActive = false;
                makeBottomMenuAdd();
            }
        }
    }

    @Override
    public void onItemLongClick(int position, View v) {
        isCabActive = true;
        AudioRecordPojo item = mAdapter.items.get(position);
        mAdapter.toggleSelection(position);
        actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));
        bottomMenu.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new OnActionBarTitleChange("Delete Blocked Number"));
        makeBottomMenuMultipleSelect();
    }

    @Override
    public void onItemCheckedChange(int position, CompoundButton button, boolean checked) {

    }
}

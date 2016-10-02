package com.videos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.fs.lib.util.OnConfirmDialog;
import com.mobiguard.MyApp;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnAttachFragment;
import com.mobiguard.OnHasVaultOperationsEvent;
import com.mobiguard.OnSetDrawerEnabledEvent;
import com.mobiguard.R;
import com.util.MyDatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public  class ListLockedVideoFileFragment extends BaseFragment implements MyEventListener {
    private final int CODE_PIC_VIDEO = 100;
    private final int CODE_PLAY_VIDEO = 101;
    private MyDatabaseHelper db;
    private ListLockedVideoFilesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<LockedVideoFiles> lockedVideoFiles;
    private int selectedItemCount=0;
    private boolean isCABActive=false;
    private    MyCAB cab;

    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.textViewEmpty)
    TextView textViewEmpty;

    @Bind(R.id.menu_action_add)
    ImageView menu_action_add;


    public ListLockedVideoFileFragment() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_video_lock_list, null);
        ButterKnife.bind(this, view);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.my_recycler_view);
        textViewEmpty= (TextView) view.findViewById(R.id.textViewEmpty);
        db = MyDatabaseHelper.getInstance(MyApp.getContext());
        loadItems();
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        cab = new MyCAB();
        return view;
    }

    @OnClick(R.id.menu_action_add)
    public void onMenuActionAddClicked(View view) {
        AddLockedVideoFragment fragment = new AddLockedVideoFragment();
        EventBus.getDefault().post(new OnAttachFragment(fragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(false));
        EventBus.getDefault().post(new OnActionBarTitleChange("Video Locker"));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void loadItems() {
        try {
            Dao<LockedVideoFiles, Long> itemsDao = db.getLockedVideosFilesDao();
            QueryBuilder<LockedVideoFiles, Long> qb=itemsDao.queryBuilder();
            qb.where().eq("isLocked",true);
            lockedVideoFiles = qb.query();
            if(lockedVideoFiles.size()>0) {
                textViewEmpty.setText("Videos in Locker");
            }else{
                textViewEmpty.setText("No Video Added.");
            }

            MyLogger.debug("Total Locked Video Files " + lockedVideoFiles.size());

            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mRecyclerView.setHasFixedSize(true);

            mAdapter = new ListLockedVideoFilesAdapter( lockedVideoFiles,R.layout.list_item_gallery);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setMyEventListener(this);
        } catch (Exception e) {
            MyLogger.debug(e.getMessage());
        }
    }

    public boolean onClickLong(int position,View v) {
        MyApp.getMainActivity().startActionMode(cab);
        mAdapter.setNewSelection(position, true);
        selectedItemCount++;
        return true;
    }

    public void onClick(int position,View v) {
        EventBus.getDefault().post(new OnHasVaultOperationsEvent(false));
        LockedVideoFiles lockedImageFile= mAdapter.items.get(position);
        String lockedImageFilePath = lockedImageFile.getPath();
        MyLogger.debug("Locked Image file path " + lockedImageFilePath);
        Uri intentUri = Uri.parse(lockedImageFilePath);
        if (isCABActive) {
            MyLogger.debug("cab is active in item click.");
            if( mAdapter.isPositionChecked(position)) {
                mAdapter.removeSelection(position);
                selectedItemCount--;
                if (selectedItemCount==0) {

                }
            } else {
                mAdapter.setNewSelection(position,true);
                selectedItemCount++;
            }
        }else {
            EventBus.getDefault().post(new OnHasVaultOperationsEvent(false));
            LockedVideoFiles lockedVideoFile = lockedVideoFiles.get(position);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(lockedVideoFile.getPath())), "video/*");
            startActivityForResult(intent, CODE_PLAY_VIDEO);
        }
    }

    @Override
    public void onItemClick(int position, View v) {
        if (isCABActive){
            mAdapter.toggleSelection(position);
        }else{
            onClick(position, v);
        }
    }

    @Override
    public void onItemLongClick(int position, View v) {
        onClickLong(position,v);
    }

    @Override
    public void onItemCheckedChange(int position, CompoundButton button, boolean checked) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MyLogger.debug("on Back Pressed in "+getClass().getName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyCAB implements ActionMode.Callback {

        private ActionMode mode;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            this.mode = mode;
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            ListLockedVideoFileFragment.this.isCABActive = true;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            MyLogger.debug("Context action menu item clicked.");
            switch (item.getItemId()) {
                case R.id.item_restore:

                    new OnConfirmDialog(getContext(),"Are You sure to restore?"){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicke
                                    try {
                                        mAdapter.restoreSelectedItems(db);
                                        mode.finish();
                                        loadItems();

                                    } catch (SQLException e) {
                                        MyLogger.debug("OnConfirmDialog item_restore exception "+e.getMessage());
                                        e.printStackTrace();
                                    }

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    }.show();

                    break;
                case R.id.item_delete:
                    new OnConfirmDialog(getContext(),"Are You sure to delete Permanently?"){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    try {
                                        mAdapter.deleteSelectedItems(db);
                                        mode.finish();
                                        loadItems();
                                    } catch (SQLException e) {
                                        MyLogger.debug("OnConfirmDialog item_delete exception "+e.getMessage());
                                        e.printStackTrace();
                                    }

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    }.show();

                    break;
            }
            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ListLockedVideoFileFragment.this.isCABActive = false;
            mAdapter.clearSelections();
        }
    }
    @Override
    public void backPressedInFragment() {
        EventBus.getDefault().post(new OnSetDrawerEnabledEvent(true));
    }
}

package com.videos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.images.ImagesVaultHelper;
import com.images.LockedImageFiles;
import com.fs.lib.util.BaseFragment;
import com.fs.lib.util.FileHelper;
import com.fs.lib.util.MyEventListener;
import com.fs.lib.util.MyLogger;
import com.mobiguard.OnActionBarTitleChange;
import com.mobiguard.OnHasVaultOperationsEvent;
import com.mobiguard.R;
import com.passwords.events.OnBackPressed;
import com.util.MyDatabaseHelper;
import com.util.Statics;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AddLockedVideoFragment extends BaseFragment implements MyEventListener {
    MyDatabaseHelper db;
    AddLockedVideoAdapter mAdapter;

    @Bind(R.id.actionbar_selected_items_count)
    TextView actionbar_selected_items_count;

    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.menu_action_save)
    ImageView menu_action_save;

    @Bind(R.id.menu_action_clear)
    ImageView menu_action_clear;

    ArrayList<LockedVideoFiles> data = new ArrayList<>();

    public AddLockedVideoFragment() {
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.db=MyDatabaseHelper.getInstance(getContext());
        View view = inflater.inflate(R.layout.fragment_video, null);
        ButterKnife.bind(this, view);
        //VideoFileFinder ff = new VideoFileFinder();
        List<LockedVideoFiles> data= null;
        try {
            data = db.getAllValidLockedVideos();
            mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
            mRecyclerView.setHasFixedSize(true);
            mAdapter = new AddLockedVideoAdapter(data, R.layout.list_item_gallery);
            mAdapter.setMyEventListener(this);
            mRecyclerView.setAdapter(mAdapter);

            view.isInEditMode();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnActionBarTitleChange("Add Video To Locker"));
    }

    @Override
    public void onItemClick(int position, View v) {
        MyLogger.debug("onClick in AddLockedVideoFragment");
        LockedVideoFiles item=mAdapter.items.get(position);
        mAdapter.toggleSelection(position);
        actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));

        // update the current selection count in bottom menu.
    }

    @Override
    public void onItemLongClick(int position, View v) {
        MyLogger.debug("onLongClick in AddLockedVideoFragment");
        onItemClick(position, v);
    }
    @OnClick(R.id.menu_action_save)
    public void menuActionSave(View view) {
        MyLogger.debug("Saving Add Image to Vault");
        saveToVault();
    }

    @OnClick(R.id.menu_action_clear)
    public void menuActionClear(View view) {
        MyLogger.debug("Clearning Add Image to Vault");
        mAdapter.clearSelections();
        actionbar_selected_items_count.setText(String.valueOf(0));
    }

    @Override
    public void onItemCheckedChange(int position,CompoundButton button, boolean checked) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      //  inflater.inflate(R.menu.save_cancel, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                MyLogger.debug("Saving Add Image to Vault");
                saveToVault();
                break;
            case R.id.action_cancel:
                MyLogger.debug("Cancelling Add Image to Vault");
                mAdapter.clearSelections();

                break;

        }
        return true;
    }

    /**
     * Move video and save inside vault    and make gif icon.
     */
    private void saveToVault() {
        List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
        for (Integer position : selectedItemPositions) {
            LockedVideoFiles lockedImageFiles = mAdapter.getItem(position);
            String actualPath = lockedImageFiles.getActualPath();
            MyLogger.debug("Loaded Picture " + actualPath);
            try {
                Dao<LockedImageFiles, Long> lockedImageDao = db.getLockedImageFilesDao();
                LockedVideoFiles lockedVideoFile = new LockedVideoFiles();
                File file = new File(actualPath);
                if (file.exists()) {
                    lockedVideoFile.setName(file.getName());
                    String outputFilePath = Statics.vaultFolder + file.getName();
                    FileHelper.moveFile(actualPath, outputFilePath);
                    MyLogger.debugPath("picked input path ", actualPath);
                    MyLogger.debugPath("picked output path ", outputFilePath);
                    //ImagesVaultHelper.generateIcon(outputFilePath, 140, 120);
                    lockedVideoFile.setActualPath(actualPath);
                    lockedVideoFile.setPath(outputFilePath);
                    lockedVideoFile.setIconPath(ImagesVaultHelper.getVideoIconPath(outputFilePath));
                    lockedVideoFile.setIsLocked(true);
                    db.createLockedVideoFile(lockedVideoFile);
                    EventBus.getDefault().post(new OnHasVaultOperationsEvent(true));
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else {
                    MyLogger.debug("Picked image path invalid.");
                    MyLogger.show(getContext(), "open failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                MyLogger.debug("cryption failed " + e.getMessage());

            }

        }
        EventBus.getDefault().post(new OnBackPressed());
    }
    @Override
    public void backPressedInFragment() {

    }
}


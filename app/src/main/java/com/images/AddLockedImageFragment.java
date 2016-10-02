package com.images;

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


import com.fs.lib.filemanager.ImageFileFinders;
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

public  class AddLockedImageFragment extends BaseFragment implements MyEventListener {
    MyDatabaseHelper db;
    AddLockedImageAdapter mAdapter;
    @Bind(com.mobiguard.R.id.actionbar_selected_items_count)
    TextView actionbar_selected_items_count;

    @Bind(com.mobiguard.R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(com.mobiguard.R.id.menu_action_save)
    ImageView menu_action_save;

    @Bind(com.mobiguard.R.id.menu_action_clear)
    ImageView menu_action_clear;
    ArrayList<LockedImageFiles> data = new ArrayList<>();

    public AddLockedImageFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.db=MyDatabaseHelper.getInstance(getContext());
        ImageFileFinders ff = new ImageFileFinders();
        ArrayList<String> fileList = ff.getFileList();
        View view = inflater.inflate(R.layout.fragment_gallery, null);
        ButterKnife.bind(this,view);
        for (int i = 0; i < fileList.size(); i++) {
            LockedImageFiles imageModel = new LockedImageFiles();
            imageModel.setName("Image " + i);
            imageModel.setPath(fileList.get(i));
            data.add(imageModel);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new AddLockedImageAdapter(data, R.layout.list_item_gallery);
        mAdapter.setMyEventListener(this);
        mRecyclerView.setAdapter(mAdapter);

        TextView textViewGallerySelectImage= (TextView) view.findViewById(R.id.textViewGallerySelectImage);
        textViewGallerySelectImage.setText("Select Picture to Lock");

        view.isInEditMode();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new OnActionBarTitleChange("Add Picture to Locker"));
    }

    @Override
    public void onItemClick(int position, View v) {
        MyLogger.debug("onClick in AddLockedVideoFragment");
        LockedImageFiles item=mAdapter.items.get(position);
        mAdapter.toggleSelection(position);
        actionbar_selected_items_count.setText(String.valueOf(mAdapter.selectedItems.size()));

    }

    @Override
    public void onItemLongClick(int position, View v) {
        MyLogger.debug("onLongClick in AddLockedVideoFragment");
    }

    @Override
    public void onItemCheckedChange(int position,CompoundButton button, boolean checked) {

    }
    @OnClick(com.mobiguard.R.id.menu_action_save)
    public void menuActionSave(View view) {
        MyLogger.debug("Saving Add Image to Vault");
        saveImagesToVault();
    }

    @OnClick(com.mobiguard.R.id.menu_action_clear)
    public void menuActionClear(View view) {
        MyLogger.debug("Clearning Add Image to Vault");
        mAdapter.clearSelections();
        actionbar_selected_items_count.setText(String.valueOf(0));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // inflater.inflate(R.menu.save_cancel,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                MyLogger.debug("Saving Add Image to Vault");
                saveImagesToVault();
                break;
            case R.id.action_cancel:
                MyLogger.debug("Cancelling Add Image to Vault");
                break;

        }
        return true;
    }

    private void saveImagesToVault() {
        List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
        for (Integer position : selectedItemPositions) {
            LockedImageFiles lockedImageFiles = mAdapter.getItem(position);
            String picturePath = lockedImageFiles.getPath();
            MyLogger.debug("Loaded Picture " + picturePath);
            try {
                Dao<LockedImageFiles, Long> lockedImageDao = db.getLockedImageFilesDao();
                LockedImageFiles lockedImageFile = new LockedImageFiles();
                File file = new File(picturePath);
                if (file.exists()) {
                    lockedImageFile.setName(file.getName());
                    String outputFilePath = Statics.vaultFolder + file.getName();
                    FileHelper.moveFile(picturePath, outputFilePath);
                    MyLogger.debugPath("picked input path ", picturePath);
                    MyLogger.debugPath("picked output path ", outputFilePath);
                    ImagesVaultHelper.generateIcon(outputFilePath, 140, 120);
                    lockedImageFile.setActualPath(picturePath);
                    lockedImageFile.setPath(outputFilePath);
                    lockedImageFile.setIconPath(ImagesVaultHelper.getIconPath(outputFilePath));
                    db.createLockedImage(lockedImageFile);
                    EventBus.getDefault().post(new OnHasVaultOperationsEvent(true));


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

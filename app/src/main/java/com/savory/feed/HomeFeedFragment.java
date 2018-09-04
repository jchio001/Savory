package com.savory.feed;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.savory.R;
import com.savory.api.clients.savory.mock.MockSavoryDataFetcher;
import com.savory.api.clients.savory.mock.models.MockDishItem;
import com.savory.upload.RequiredDishInfoActivity;
import com.savory.utils.Constants;
import com.savory.utils.FileUtils;
import com.savory.utils.PermissionUtils;
import com.savory.utils.UIUtils;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFeedFragment extends Fragment{

    public static HomeFeedFragment newInstance() {
        HomeFeedFragment fragment = new HomeFeedFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.feed_list) RecyclerView feedList;
    @BindString(R.string.choose_image_from) String chooseImageFrom;

    protected HomeFeedAdapter homeFeedAdapter;
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_feed, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        homeFeedAdapter = new HomeFeedAdapter(getContext());
        feedList.setAdapter(homeFeedAdapter);

        MockSavoryDataFetcher mockDataFetcher = new MockSavoryDataFetcher(dataFetchListener);
        mockDataFetcher.fetchData();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar.setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    private final MockSavoryDataFetcher.Listener dataFetchListener = new MockSavoryDataFetcher.Listener() {
        @Override
        public void onDishItemsFetched(List<MockDishItem> items) {
            homeFeedAdapter.setFeedItems(items);
        }
    };

    /** Starts the flow to add a dish via uploading from gallery */
    private void addWithGallery() {
        if (PermissionUtils.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, getActivity())) {
            openFilePicker();
        } else {
            PermissionUtils.requestPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Constants.GALLERY_CODE);
        }
    }

    private void openFilePicker() {
        Intent getIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        Intent chooserIntent = Intent.createChooser(getIntent, chooseImageFrom);
        startActivityForResult(chooserIntent, Constants.GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Constants.GALLERY_CODE || resultCode != Activity.RESULT_OK) {
            return;
        }

        String chosenFilePath = FileUtils.getGalleryImagePath(data, getActivity());
        if (chosenFilePath == null) {
            UIUtils.showLongToast(R.string.gallery_choice_fail, getActivity());
            return;
        }

        Intent intent = new Intent(getActivity(), RequiredDishInfoActivity.class)
                .putExtra(Constants.PHOTO_FILE_PATH_KEY, chosenFilePath);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {
        if (requestCode != Constants.GALLERY_CODE
                || grantResults.length <= 0
                || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // External storage permission granted
        openFilePicker();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        UIUtils.loadActionBarIcon(
                menu,
                R.id.upload_from_gallery,
                IoniconsIcons.ion_android_folder,
                getActivity());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_from_gallery:
                addWithGallery();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

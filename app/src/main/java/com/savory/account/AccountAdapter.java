package com.savory.account;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savory.R;
import com.savory.api.clients.savory.models.Account;
import com.savory.api.clients.savory.models.AccountInfo;
import com.savory.api.clients.savory.models.Photo;
import com.savory.photos.PhotosCellViewHolder;
import com.savory.ui.HeaderPagingAdapter;
import com.savory.ui.HeaderPagingOnScrollListener;

import java.util.List;

public class AccountAdapter extends HeaderPagingAdapter<AccountInfo, Photo> {

    protected Account account;

    public AccountAdapter(int pageSize) {
        super(pageSize);
    }

    @Override
    public int getCount() {
        return (account != null ? 1 : 0) + (objects.size() / 3) + (isNextPageAvailable ? 1 : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else if (1 <= position && position < 1 + (objects.size() / 3)) {
            return 2;
        } else {
            return HeaderPagingOnScrollListener.PROGRESS_BAR_FOOTER_ID;
        }
    }

    @Override
    public void addHeader(AccountInfo accountInfo) {
        List<Photo> firstPageOfPhotos = accountInfo.getPhotos();
        account = accountInfo.getAccount();
        objects.addAll(firstPageOfPhotos);
        isNextPageAvailable = (firstPageOfPhotos.size() == pageSize);
        notifyDataSetChanged();
    }

    @Override
    @Nullable
    public Integer getLastId() {
        int currentSize = objects.size();
        return currentSize != 0 ? objects.get(currentSize - 1).getId() : null;
    }

    @Override
    @NonNull
    public View renderHeaderView(View convertView, ViewGroup parent) {
        AccountInfoViewHolder accountInfoViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_info_cell, parent, false);
            accountInfoViewHolder = new AccountInfoViewHolder(convertView);
            convertView.setTag(accountInfoViewHolder);
        } else {
            accountInfoViewHolder = (AccountInfoViewHolder) convertView.getTag();
        }

        accountInfoViewHolder.populate(account);
        return convertView;
    }

    @Override
    @NonNull
    public View renderObjectView(int position, View convertView, ViewGroup parent) {
        PhotosCellViewHolder photosCellViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photos_cell, parent, false);
            photosCellViewHolder = new PhotosCellViewHolder(convertView);
            convertView.setTag(photosCellViewHolder);
        } else {
            photosCellViewHolder = (PhotosCellViewHolder) convertView.getTag();
        }

        int startIndex = ((position - 1) * 3);
        photosCellViewHolder.setContent(
            objects.get(startIndex),
            objects.get(startIndex + 1),
            objects.get(startIndex + 2));

        return convertView;
    }

    @Override
    @NonNull
    public View renderProgressBarView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_bar_cell, parent, false);
        }

        return convertView;
    }
}

package com.savory.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savory.R;
import com.savory.api.clients.savory.models.Account;
import com.savory.api.clients.savory.models.AccountInfo;
import com.savory.api.clients.savory.models.Photo;
import com.savory.photos.PhotosCellViewHolder;
import com.savory.ui.AbstractPagingAdapter;

public class AccountAdapter extends AbstractPagingAdapter<Photo> {

    private Account account;

    public AccountAdapter(int pageSize) {
        super(pageSize);
    }

    @Override
    public int getCount() {
        return (account != null ? 1 : 0) + (objects.size() / 3) + (isPageAvailable ? 1 : 0);
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return account;
        } else if (1 <= position && position < 1 + (objects.size() / 3)) {
            return objects.get(position - 1);
        } else {
            return null;
        }
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
            return 3;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case 1:
                return renderAccountCell(convertView, parent);
            case 2:
                return renderPhotosCell(position, convertView, parent);
            default:
                return renderProgressBarCell(convertView, parent);
        }
    }

    public void addAccountInfo(AccountInfo accountInfo) {
        this.account = accountInfo.getAccount();
        objects.addAll(accountInfo.getPhotos());
        isPageAvailable = (objects.size() == pageSize);
        notifyDataSetChanged();
    }

    @Override
    public int getLastId() {
        int currentSize = objects.size();

        if (currentSize > 0) {
            return objects.get(objects.size() - 1).getId();
        } else {
            return -1;
        }
    }

    private View renderAccountCell(View convertView, ViewGroup parent) {
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

    private View renderPhotosCell(int position, View convertView, ViewGroup parent) {
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

    private View renderProgressBarCell(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_bar_cell, parent, false);
        }

        return convertView;
    }
}

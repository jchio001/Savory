package com.savory.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.savory.R;
import com.savory.api.models.Account;
import com.savory.api.models.AccountInfo;
import com.savory.api.models.Photo;
import com.savory.photos.PhotosCellViewHolder;

import java.util.ArrayList;

public class AccountAdapter extends BaseAdapter {

    private Account account;
    private ArrayList<Photo> photos = new ArrayList<>(18);

    public AccountAdapter(AccountInfo accountInfo) {
        this.account = accountInfo.getAccount();
        photos.addAll(accountInfo.getPhotos());
    }

    @Override
    public int getCount() {
        return 2 + (photos.size() / 3);
    }

    @Override
    public Object getItem(int position) {
        return account;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else if (1 <= position && position < 1 + (photos.size() / 3)) {
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
            photos.get(startIndex),
            photos.get(startIndex + 1),
            photos.get(startIndex + 2));

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

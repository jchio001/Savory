package com.savory.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.savory.R;
import com.savory.api.models.Account;
import com.savory.api.models.AccountInfo;
import com.savory.api.models.Photo;

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
        return 1;
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
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
}

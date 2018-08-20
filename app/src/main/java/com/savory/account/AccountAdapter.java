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
import com.savory.ui.AbstractPagingAdapter;
import com.savory.ui.PagingOnScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountAdapter extends AbstractPagingAdapter<Photo> {

    protected Account account;

    public AccountAdapter(int pageSize) {
        super(pageSize);
    }

    @Override
    public int getCount() {
        return (account != null ? 1 : 0) + (objects.size() / 3) + (isNextPageAvailable ? 1 : 0);
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
            return PagingOnScrollListener.PROGRESS_BAR_FOOTER_ID;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onFirstPageResponse(Response firstPageCall) {
        if (firstPageCall.isSuccessful()) {
            AccountInfo accountInfo = (AccountInfo) firstPageCall.body();

            List<Photo> firstPageOfPhotos = accountInfo.getPhotos();

            account = accountInfo.getAccount();
            objects.addAll(firstPageOfPhotos);
            isNextPageAvailable = (firstPageOfPhotos.size() == pageSize);
            notifyDataSetChanged();
        } else {
            // TODO: Do important things here
        }
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
        List<Photo> firstPageOfPhotos = accountInfo.getPhotos();

        this.account = accountInfo.getAccount();
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

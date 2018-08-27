package com.savory.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.savory.R;
import com.savory.api.clients.savory.SavoryClient;
import com.savory.api.clients.savory.models.AccountInfo;
import com.savory.api.clients.savory.models.Photo;
import com.savory.ui.HeaderPagingOnScrollListener;
import com.savory.ui.PagingOnScrollListener.PageSupplier;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class AccountFragment extends Fragment {

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.list_view) ListView ListView;

    private HeaderPagingOnScrollListener<AccountInfo, Photo> pagingOnScrollListener;
    protected AccountAdapter accountAdapter = new AccountAdapter(15);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SavoryClient savoryClient = SavoryClient.get();

        PageSupplier<Photo> photoPageSupplier = new PageSupplier<Photo>() {
            @Override
            public Call<List<Photo>> supplyPage() {
                return savoryClient.getPageOfMyPhotos(accountAdapter.getLastId());
            }

            @Override
            public void onFirstPageLoaded() {
                progressBar.setVisibility(View.GONE);
                ListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {}
        };

        pagingOnScrollListener = new HeaderPagingOnScrollListener<>(savoryClient.getMyAccountInfo(),
                photoPageSupplier);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_generic_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (!accountAdapter.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            ListView.setVisibility(View.VISIBLE);
        }

        ListView.setAdapter(accountAdapter);
        ListView.setOnScrollListener(pagingOnScrollListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar.setTitle(R.string.profile);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        pagingOnScrollListener.cancelPendingPage();
    }
}

package com.savory.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.savory.R;
import com.savory.api.clients.savory.SavoryClient;
import com.savory.api.clients.savory.models.Photo;
import com.savory.ui.PagingOnScrollListener;
import com.savory.ui.PagingOnScrollListener.PageSupplier;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class AccountFragment extends Fragment {

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.profile_listview) ListView profileListView;

    private PagingOnScrollListener<Photo> pagingOnScrollListener;
    protected AccountAdapter accountAdapter = new AccountAdapter(15);

    public AccountFragment() {
        final SavoryClient savoryClient = SavoryClient.get();

        PageSupplier<Photo> photoPageSupplier = new PageSupplier<Photo>() {
            @Override
            public Call<List<Photo>> supplyPage() {
                return savoryClient.getPageOfMyPhotos(accountAdapter.getLastId());
            }

            @Override
            public void onFirstPageLoaded() {
                progressBar.setVisibility(View.GONE);
                profileListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {
            }
        };

        pagingOnScrollListener = new PagingOnScrollListener<>(photoPageSupplier,
                                                              savoryClient.getMyAccountInfo());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (!accountAdapter.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            profileListView.setVisibility(View.VISIBLE);
        }

        profileListView.setAdapter(accountAdapter);
        profileListView.setOnScrollListener(pagingOnScrollListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        pagingOnScrollListener.cancelPendingPage();
    }
}

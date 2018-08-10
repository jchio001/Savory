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
import com.savory.api.SavoryClient;
import com.savory.api.models.AccountInfo;
import com.savory.api.models.Photo;
import com.savory.data.SPClient;
import com.savory.ui.PagingOnScrollListener;
import com.savory.ui.PagingOnScrollListener.PageSupplier;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.profile_listview) ListView profileListView;

    protected SavoryClient savoryClient;
    private SPClient spClient;

    protected String savoryToken;

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

        savoryClient = SavoryClient.get();
        spClient = new SPClient(getContext());
        savoryToken = spClient.retrieveSavoryToken();

        savoryClient.getMyAccountInfo(savoryToken)
            .enqueue(new Callback<AccountInfo>() {
                @Override
                public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        profileListView.setVisibility(View.VISIBLE);

                        final AccountAdapter accountAdapter = new AccountAdapter(response.body());
                        profileListView.setAdapter(accountAdapter);
                        profileListView.setOnScrollListener(new PagingOnScrollListener<>(
                            new PageSupplier<Photo>() {
                                @Override
                                public Call<List<Photo>> supplyPage() {
                                    return savoryClient.getMyPhotos(savoryToken,
                                        accountAdapter.getLastId());
                                }
                            }));
                    }
                }

                @Override
                public void onFailure(Call<AccountInfo> call, Throwable t) {

                }
            });
    }
}

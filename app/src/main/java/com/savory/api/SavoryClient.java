package com.savory.api;

import android.support.annotation.NonNull;

import com.savory.api.models.AccountInfo;
import com.savory.api.models.Photo;
import com.savory.api.models.SavoryToken;

import java.util.List;

import retrofit2.Call;

public class SavoryClient {

    private static final String BASE_URL = "http://savory-backend.herokuapp.com";

    private static SavoryClient instance;

    private SavoryService savoryService;

    public SavoryClient() {

        savoryService = RetrofitBuilderFactory.createBaseRetrofit()
            .baseUrl(BASE_URL)
            .build()
            .create(SavoryService.class);
    }

    public static SavoryClient get() {
        if (instance == null) {
            synchronized (SavoryClient.class) {
                if (instance == null) {
                    instance = new SavoryClient();
                }
            }
        }

        return instance;
    }

    public Call<SavoryToken> connect(String socialPlatformToken) {
        return savoryService.connectWithSocial(socialPlatformToken);
    }

    public Call<AccountInfo> getMyAccountInfo(@NonNull String savoryToken) {
        return savoryService.getMyAccountInfo(savoryToken);
    }

    public Call<List<Photo>> getMyPhotos(@NonNull String savoryToken,
                                         int lastId) {
        return savoryService.getMyPhotos(savoryToken, lastId);
    }
}

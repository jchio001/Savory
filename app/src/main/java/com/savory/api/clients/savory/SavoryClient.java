package com.savory.api.clients.savory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.savory.api.clients.savory.models.AccountInfo;
import com.savory.api.clients.savory.models.Photo;
import com.savory.api.clients.savory.models.SavoryToken;
import com.savory.api.resources.RetrofitBuilderFactory;

import java.util.List;

import retrofit2.Call;

public class SavoryClient {

    private static final String BASE_URL = "http://savory-backend.herokuapp.com/";

    private static SavoryClient instance;

    private SavoryService savoryService;

    private String savoryToken;

    private SavoryClient() {
        savoryService = RetrofitBuilderFactory.createBase()
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

    public void setSavoryToken(@NonNull String savoryToken) {
        this.savoryToken = savoryToken;
    }

    public Call<SavoryToken> connect(@NonNull String socialPlatformToken) {
        return savoryService.connectWithSocial(socialPlatformToken);
    }

    public Call<List<Photo>> getPageOfPhotos(@Nullable Integer lastId)  {
        return savoryService.getPageOfPhotos(savoryToken, lastId);
    }

    public Call<AccountInfo> getMyAccountInfo() {
        return savoryService.getMyAccountInfo(savoryToken);
    }

    public Call<List<Photo>> getPageOfMyPhotos(@Nullable Integer lastId) {
        return savoryService.getPageOfMyPhotos(savoryToken, lastId);
    }
}

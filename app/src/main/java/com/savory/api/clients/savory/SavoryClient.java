package com.savory.api.clients.savory;

import android.support.annotation.NonNull;

import com.savory.api.clients.savory.models.AccountInfo;
import com.savory.api.clients.savory.models.Photo;
import com.savory.api.clients.savory.models.SavoryToken;
import com.savory.api.resources.RetrofitBuilderFactory;
import com.savory.location.LocationManager;

import java.util.List;

import retrofit2.Call;

public class SavoryClient {

    private static final String BASE_URL = "http://savory-backend.herokuapp.com/";

    private static SavoryClient instance;

    private SavoryService savoryService;

    private LocationManager locationManager = new LocationManager();

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

    public Call<SavoryToken> connect(@NonNull String socialPlatformToken) {
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

package com.savory.api;

import android.support.annotation.NonNull;

import com.savory.api.models.AccountInfo;
import com.savory.api.models.Photo;
import com.savory.api.models.SavoryToken;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class SavoryClient {

    private static final String BASE_URL = "http://savory-backend.herokuapp.com";

    private static SavoryClient instance;

    private SavoryService savoryService;

    public SavoryClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build();

        savoryService = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
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

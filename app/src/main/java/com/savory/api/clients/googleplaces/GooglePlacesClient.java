package com.savory.api.clients.googleplaces;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.savory.BuildConfig;
import com.savory.api.clients.googleplaces.models.Places;
import com.savory.api.resources.RetrofitBuilderFactory;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlacesClient {

    public interface Listener {
        void onPlacesFetched(Places places);

        void onPlaceFetchFail();
    }

    private static final String BASE_URL = "https://maps.googleapis.com/";
    private static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;

    private static GooglePlacesClient instance;

    private GooglePlacesService googlePlacesService;
    private Handler backgroundHandler;
    private @Nullable Listener listener;
    private @Nullable Call<Places> currentPlacesCall;

    public static GooglePlacesClient get() {
        if (instance == null) {
            synchronized (GooglePlacesClient.class) {
                if (instance == null) {
                    instance = new GooglePlacesClient();
                }
            }
        }

        return instance;
    }

    private GooglePlacesClient() {
        googlePlacesService = RetrofitBuilderFactory.createBase()
            .baseUrl(BASE_URL)
            .build()
            .create(GooglePlacesService.class);

        HandlerThread handlerThread = new HandlerThread(UUID.randomUUID().toString());
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    public void shutdown() {
        this.listener = null;
        cancelCurrentCallAndMaybeRunNext(null);
    }

    private void cancelCurrentCallAndMaybeRunNext(@Nullable final Call<Places> nextCall) {
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                if (currentPlacesCall != null) {
                    currentPlacesCall.cancel();
                }

                if (nextCall != null) {
                    currentPlacesCall = nextCall;
                    currentPlacesCall.enqueue(placesCallback);
                }
            }
        });
    }

    public void getPlaces(String keyword, String location) {
        Call<Places> nextCall = googlePlacesService.getPlaces(GOOGLE_API_KEY, keyword, location);
        cancelCurrentCallAndMaybeRunNext(nextCall);
    }

    private Callback<Places> placesCallback = new Callback<Places>() {
        @Override
        public void onResponse(@NonNull Call<Places> call, @NonNull Response<Places> response) {
            if (listener == null) {
                return;
            }

            if (response.isSuccessful()) {
                listener.onPlacesFetched(response.body());
            } else {
                listener.onPlaceFetchFail();
            }
        }

        @Override
        public void onFailure(@NonNull Call<Places> call, @NonNull Throwable t) {
            // TODO: Handle failure while dealing with cancelled calls
        }
    };
}

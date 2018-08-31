package com.savory.api.clients.yelp;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.savory.api.clients.yelp.models.RestaurantSearchResults;
import com.savory.api.resources.RetrofitBuilderFactory;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YelpPlacesClient {

    public interface Listener {
        void onPlacesFetched(RestaurantSearchResults results);

        void onPlaceFetchFail();
    }

    private static final String BASE_URL = "https://api.yelp.com";
    private static final int DEFAULT_NUM_RESTAURANTS = 10;
    private static final String BEST_MATCH = "best_match";
    private static final String DISTANCE = "distance";

    private static YelpPlacesClient instance;

    private YelpService yelpService;
    private Handler backgroundHandler;
    private @Nullable Listener listener;
    private @Nullable Call<RestaurantSearchResults> currentPlacesCall;

    public static YelpPlacesClient get() {
        if (instance == null) {
            synchronized (YelpPlacesClient.class) {
                if (instance == null) {
                    instance = new YelpPlacesClient();
                }
            }
        }

        return instance;
    }

    private YelpPlacesClient() {
        yelpService = RetrofitBuilderFactory.createBase(new YelpAuthInterceptor())
            .baseUrl(BASE_URL)
            .build()
            .create(YelpService.class);

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

    private void cancelCurrentCallAndMaybeRunNext(@Nullable final Call<RestaurantSearchResults> nextCall) {
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
        Call<RestaurantSearchResults> nextCall = yelpService.fetchRestaurants(
                keyword,
                location,
                DEFAULT_NUM_RESTAURANTS,
                keyword.isEmpty() ? DISTANCE : BEST_MATCH);
        cancelCurrentCallAndMaybeRunNext(nextCall);
    }

    private Callback<RestaurantSearchResults> placesCallback = new Callback<RestaurantSearchResults>() {
        @Override
        public void onResponse(@NonNull Call<RestaurantSearchResults> call, @NonNull Response<RestaurantSearchResults> response) {
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
        public void onFailure(@NonNull Call<RestaurantSearchResults> call, @NonNull Throwable t) {
            // TODO: Handle failure while dealing with cancelled calls
        }
    };
}

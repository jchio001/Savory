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

public class YelpRestaurantClient {

    public interface Listener {
        void onRestaurantFetched(RestaurantSearchResults results);

        void onRestaurantFetchFail();
    }

    private static final String BASE_URL = "https://api.yelp.com";
    private static final int DEFAULT_NUM_RESTAURANTS = 10;
    private static final String BEST_MATCH = "best_match";
    private static final String DISTANCE = "distance";

    private static YelpRestaurantClient instance;

    private YelpService yelpService;
    private Handler backgroundHandler;
    protected @Nullable Listener listener;
    protected @Nullable Call<RestaurantSearchResults> currentRestaurantCall;

    public static YelpRestaurantClient get() {
        if (instance == null) {
            synchronized (YelpRestaurantClient.class) {
                if (instance == null) {
                    instance = new YelpRestaurantClient();
                }
            }
        }

        return instance;
    }

    private YelpRestaurantClient() {
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
                if (currentRestaurantCall != null) {
                    currentRestaurantCall.cancel();
                }

                if (nextCall != null) {
                    currentRestaurantCall = nextCall;
                    currentRestaurantCall.enqueue(restaurantCallback);
                }
            }
        });
    }

    public void getRestaurant(String keyword, String location) {
        Call<RestaurantSearchResults> nextCall = yelpService.fetchRestaurants(
                keyword,
                location,
                DEFAULT_NUM_RESTAURANTS,
                keyword.isEmpty() ? DISTANCE : BEST_MATCH);
        cancelCurrentCallAndMaybeRunNext(nextCall);
    }

    protected Callback<RestaurantSearchResults> restaurantCallback =
        new Callback<RestaurantSearchResults>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantSearchResults> call,
                                   @NonNull Response<RestaurantSearchResults> response) {
                if (listener == null) {
                    return;
                }

                if (response.isSuccessful()) {
                    listener.onRestaurantFetched(response.body());
                } else {
                    listener.onRestaurantFetchFail();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantSearchResults> call, @NonNull Throwable t) {
                // TODO: Handle failure while dealing with cancelled calls
            }
    };
}

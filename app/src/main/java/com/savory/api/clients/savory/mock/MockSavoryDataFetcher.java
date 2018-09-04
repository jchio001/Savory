package com.savory.api.clients.savory.mock;

import android.os.Handler;

import com.savory.api.clients.savory.mock.models.MockDishItem;

import java.util.List;

public class MockSavoryDataFetcher {

    private static final long MILLIS_DELAY_BEFORE_RETURN = 1000L;

    public interface Listener {
        void onDishItemsFetched(List<MockDishItem> items);
    }

    private final Runnable fetchDataTask = new Runnable() {
        @Override
        public void run() {
            List<MockDishItem> dishItems = mockDataProvider.getMockDishItems();
            listener.onDishItemsFetched(dishItems);
        }
    };

    protected Listener listener;
    private Handler handler;
    protected MockDataProvider mockDataProvider;

    public MockSavoryDataFetcher(Listener listener) {
        this.listener = listener;
        this.handler = new Handler();
        this.mockDataProvider = new MockDataProvider();
    }

    public void fetchData() {
        handler.postDelayed(fetchDataTask, MILLIS_DELAY_BEFORE_RETURN);
    }
}

package com.savory.api.clients.savory.mock;

import com.savory.api.clients.savory.mock.models.MockDishItem;

import java.util.List;

public class MockSavoryDataFetcher {

    public interface Listener {
        void onFeedFetched(List<MockDishItem> items);
    }


}

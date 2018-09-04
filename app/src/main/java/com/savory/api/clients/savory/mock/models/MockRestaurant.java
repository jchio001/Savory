package com.savory.api.clients.savory.mock.models;

public class MockRestaurant {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public MockRestaurant setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public MockRestaurant setAddress(String address) {
        this.address = address;
        return this;
    }
}

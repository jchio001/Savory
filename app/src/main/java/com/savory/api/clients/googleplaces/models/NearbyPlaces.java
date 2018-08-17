package com.savory.api.clients.googleplaces.models;

import java.util.List;

public class NearbyPlaces {

    private List<Place> results;

    private String status;

    public List<Place> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }
}

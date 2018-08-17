package com.savory.api.clients.googleplaces.models;

import java.util.List;

public class NearbyPlaces {

    private List<Place> nearbyPlaces;

    private String status;

    public List<Place> getNearbyPlaces() {
        return nearbyPlaces;
    }

    public String getStatus() {
        return status;
    }
}

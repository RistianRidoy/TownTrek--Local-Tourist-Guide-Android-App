package com.example.towntrek.AviationDirectionHelpers;

import com.google.gson.annotations.SerializedName;

public class AviationRouteData {
    @SerializedName("flight_iata")
    private String flightIata;

    // Add more fields as needed

    public String getFlightIata() {
        return flightIata;
    }

    // Implement getters for other fields
}

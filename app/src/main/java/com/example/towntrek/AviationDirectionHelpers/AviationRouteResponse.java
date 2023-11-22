package com.example.towntrek.AviationDirectionHelpers;

import com.google.gson.annotations.SerializedName;

public class AviationRouteResponse {
    @SerializedName("data")
    private AviationRouteData data;

    public AviationRouteData getData() {
        return data;
    }
}


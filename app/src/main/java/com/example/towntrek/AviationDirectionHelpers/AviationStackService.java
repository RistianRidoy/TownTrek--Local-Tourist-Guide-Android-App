package com.example.towntrek.AviationDirectionHelpers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AviationStackService {
    @GET("routes") // Update the endpoint to "routes" for fetching airline routes
    Call<AviationRouteResponse> getAviationRoute(
            @Query("access_key") String accessKey,
            @Query("destination") String destination
    );
}
package com.example.rumi.tourassistant.Nearby;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyServiceApi {

    @GET()
    Call<NearbyResponse>getNearbyPlaces(@Url String endUrl);

    @GET()
    Call<NearbyResponse>getNextPageTokenResult(@Url String endUrl);
}

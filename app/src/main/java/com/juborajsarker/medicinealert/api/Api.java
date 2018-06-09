package com.juborajsarker.medicinealert.api;

import com.juborajsarker.medicinealert.model.place.ModelPlace;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {

    String BASE_URL = "https://maps.googleapis.com/maps/api/place/search/";

    // String REST_URL = "=false&key=AIzaSyDifthkIH5dAuPeg4rnzs3hjBMI0_5g8hE";




    @GET
    Call<ModelPlace> getResults(@Url String url);

}

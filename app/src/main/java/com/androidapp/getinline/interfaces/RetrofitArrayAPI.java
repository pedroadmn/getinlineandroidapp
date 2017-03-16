package com.androidapp.getinline.interfaces;

import com.androidapp.getinline.entities.Establishment;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

public interface RetrofitArrayAPI {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us the Establishments.
    */
    @GET("/api/allEstablishments")
    Call<List<Establishment>> getEstablishmentDetails();

}

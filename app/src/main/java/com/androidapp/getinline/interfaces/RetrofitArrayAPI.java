package com.androidapp.getinline.interfaces;

import com.androidapp.getinline.entities.Establishment;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;


public interface RetrofitArrayAPI {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us the Establishments.
    */
    @GET("/api/allEstablishments")
    Call<List<Establishment>> getEstablishmentDetails();

    @FormUrlEncoded
    @POST("/api/addClientQueue")
    Call<ResponseBody>  getUserLogin(
            @Field("nameEstablishment") String nameEstablishment,
            @Field("nameClient") String nameClient,
            /*@Field("currentHour") String currentHour,*/
            @Field("idClient") String idClient,
            @Field("tokenGCM") String tokenGCM,
            @Field("est_id") String establishmentId,
            @Field("login") String loginClient
    );

}

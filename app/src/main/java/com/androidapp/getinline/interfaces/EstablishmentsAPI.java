package com.androidapp.getinline.interfaces;

import com.androidapp.getinline.entities.Establishment;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;


public interface EstablishmentsAPI {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us the Establishments.
    */
    @GET("/api/allEstablishments")
    Call<List<Establishment>> getEstablishmentDetails();

    /**
     * Post to add client to Establishment line
     *
     * @param nameEstablishment Establishment name
     * @param nameClient        Client name
     * @param idClient          Client id
     * @param tokenGCM          Token FCM
     * @param establishmentId   Establishment id
     * @param loginClient       Client login
     * @return Callback Response
     */

    @FormUrlEncoded
    @POST("/api/addClientQueue")
    Call<ResponseBody> postClientToQueue(
            @Field("nameEstablishment") String nameEstablishment,
            @Field("nameClient") String nameClient,
            /*@Field("currentHour") String currentHour,*/
            @Field("idClient") String idClient,
            @Field("tokenGCM") String tokenGCM,
            @Field("est_id") String establishmentId,
            @Field("login") String loginClient
    );
}

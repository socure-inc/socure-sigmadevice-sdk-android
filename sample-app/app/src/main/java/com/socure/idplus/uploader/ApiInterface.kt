package com.socure.idplus.uploader

import com.socure.idplus.devicerisk.androidsdk.model.InformationRequest
import com.socure.idplus.devicerisk.androidsdk.model.InformationResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("track/")
    fun getTrackDetails(@Query("data") data: String?): Call<Int?>?

    @Headers("Content-Type: application/json")
    @POST("api/3.0/EmailAuthScore")
    fun getInformationResponse(
        @Body informationRequest: InformationRequest?,
        @Header("Authorization") auth: String?
    ): Call<InformationResponse?>?
}
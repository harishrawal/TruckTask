package com.telematics.asset.trucktask.api

import com.telematics.asset.trucktask.model.VehicleDataResponse
import com.telematics.asset.trucktask.model.postdata.PostData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("mobile/configure/v1/task")
    suspend fun getVehicleData(@Body post:PostData):Response<VehicleDataResponse>
}
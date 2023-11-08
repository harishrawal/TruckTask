package com.telematics.asset.trucktask.repository

import com.telematics.asset.trucktask.model.VehicleDataResponse
import com.telematics.asset.trucktask.model.postdata.PostData
import retrofit2.Response

interface MainRepository {
    suspend fun getVehicleDetails(postData: PostData): Response<VehicleDataResponse>
}